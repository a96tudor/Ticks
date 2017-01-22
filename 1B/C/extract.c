/**
 *              extract.c -- C/C++ Assessed Exercise
 *
 *          Extracts the messages contents and returns them as
 *                      images/ text files
 *
 *       Tudor Avram -- Homerton College, University of Cambridge
 *                      tma33@cam.ac.uk
 */

#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <netinet/in.h>

#define BUFSIZE 20

#define IP_HLEN(lenver) (lenver & 0x0f)
#define IP_VER(lenver) (lenver >> 4)
#define TCP_OFF(offres) (offres >> 4)

/**
 *      IP header definition
*/

typedef struct ip_header {
    uint8_t hlenver; /*Version + Internet Header Length*/
    uint8_t tos; /*Type of service*/
    uint16_t len; /*Total length*/
    uint16_t id; /*Identification*/
    uint16_t off; /*Flags & Fragment offset*/
    uint8_t ttl; /*Time to live*/
    uint8_t p; /*Protocol*/
    uint16_t chksum; /*Header cheksum*/
    uint32_t src; /*Source address*/
    uint32_t dest; /*Destinatil address*/
} IP;

/**
 *      TCP heaader definition
*/

typedef struct tcp_header{
    uint16_t src; /*Source Port*/
    uint16_t dst; /*Destination Port*/
    uint32_t seqno; /*Sequence Number*/
    uint32_t ackno; /*Acknowledgment Number*/
    uint8_t off; /*Data Offset + Reserved*/
    uint8_t ctrl; /*Reserved + Control Bits*/
    uint16_t win; /*Window*/
    uint16_t chksum; /*Header checksum*/
    uint16_t urgptr; /*Urgent Pointer*/
} TCP;

/**
 *      Function that reads an 8 bits int
 *
 *      @param p    The pointer to reaad from
 *      @returns    The 8-bites stream found at the specified position as an int
 */

uint8_t read_int_8(char **p) {
    uint8_t result = (uint8_t) (**p);
    (*p)++;
    return result;
}

/**
 *      Function that reads an 16 bits int
 *
 *      @param p    The pointer to reaad from
 *      @returns    The 16 bites stream found at the specified position as an int
 */

uint16_t read_int_16(char **p) {
    uint16_t result = 0;
    result = ((uint16_t)read_int_8(p)) << 8;
    result |= (uint16_t)read_int_8(p);
    return result;
}

/**
 *      Function that reads an 32 bits int
 *
 *      @param p    The pointer to reaad from
 *      @returns    The 16 bits stream found at the specified position as an int
 */

uint32_t read_int_32(char **p) {
    uint32_t result = 0;
    result = ((uint32_t)read_int_16(p)) << 16;
    result |= (uint32_t)read_int_16(p);
    return result;
}

/**
 *   Function that reads the IP header
*/

void read_IP_header(char *buf, IP *ip) {
    ip -> hlenver = read_int_8(&buf);
    ip -> tos = read_int_8(&buf);
    ip -> len = read_int_16(&buf);
    ip -> id = read_int_16(&buf);
    ip -> off = read_int_16(&buf);
    ip -> ttl = read_int_8(&buf);
    ip -> p = read_int_8(&buf);
    ip -> chksum = read_int_16(&buf);
    ip -> src = read_int_32(&buf);
    ip -> dest = read_int_32(&buf);
}

/**
 *  Function that reads the TCP header
 */

void read_TCP_header(char *buf, TCP *tcp) {
    tcp -> src = read_int_16(&buf);
    tcp -> dst = read_int_16(&buf);
    tcp -> seqno = read_int_32(&buf);
    tcp -> ackno = read_int_32(&buf);
    tcp -> off = read_int_8(&buf);
    tcp -> ctrl = read_int_8(&buf);
    tcp -> win = read_int_16(&buf);
    tcp -> chksum = read_int_16(&buf);
    tcp -> urgptr = read_int_16(&buf);

}

int main(int argc, char **argv) {
    FILE *fin, *fout;

    char buff[BUFSIZE];
    char *main_buff;
    int total_ip_packets = 0;
    uint32_t source, destination;

    if (argc != 3) {
        /* illegal number of arguments */
        printf("Usage: extract <in> <out>\n");
        printf("%s\n",*argv);
        return 1; // Returning error code 1
    }

    if ((fin = fopen(argv[1], "rb")) == 0) {
        /*wrong source file*/
        printf("Unable to open the log file to extract from\n");
        return 2; // Returning error code 2
    }

    fout = fopen(argv[2], "w");

    while (!feof(fin)) {
        IP *ip = (IP*)malloc(sizeof(IP));
        TCP *tcp = (TCP*)malloc(sizeof(TCP));

        char *curpntr = buff;

        fread(buff, sizeof(char), BUFSIZE, fin);
        if (feof(fin)) {
            break; // we reached the end of the file, so no need to move on
        }

        read_IP_header(curpntr, ip);

        int hd_len = IP_HLEN(ip -> hlenver);
        int total_len = (ip -> len);

        if (total_ip_packets == 0) {
            /*
             *      The client has to initiate the connection by sending a packet to the server.
             *      This way, the source of the 1st packet is actually the destination and vice-versa.
             */
            source = (ip->dest);
            destination = (ip->src);
        }

        total_ip_packets++;

        int rem = (4 * hd_len - BUFSIZE); /*getting the remaining bytes of the header*/
        fseek(fin, rem, SEEK_CUR); /*skipping the remainder of the header*/

        fread(buff, sizeof(char), BUFSIZE, fin);
        curpntr = buff;
        read_TCP_header(curpntr, tcp);

        int offset = TCP_OFF(tcp -> off);

        int rem1 = (4*offset - BUFSIZE);
        fseek(fin, rem1, SEEK_CUR);
        int rem2 = total_len - (2*BUFSIZE + rem + rem1);

        main_buff = (char*)malloc(rem2 * sizeof(char));
        fread(main_buff, sizeof(char), rem2, fin);

        if (ip -> src == source && ip -> dest == destination) fwrite(main_buff, rem2, sizeof(char), fout);
    }

    fclose(fin);
    fclose(fout);

    return 0;
}