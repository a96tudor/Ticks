/**
 *
 *              sumary.c -- C/C++ Assessed Exercise
 *
 *      Reads a message and identifies :
 *          -   The IP addresses of the sender & destination
 *          -   The value of the first IP packet’s header size field
 *          -   The length of the first IP packet in bytes
 *          -   The value of the first TCP packet’s header size field
 *          -   The total number of IP packets in the trace
 *
 *     Tudor Avram -- Homerton College, University of Cambridge
 *                      tma33@cam.ac.uk
 *
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

/**
 *   Function that returns the IP address
 *   @param addr    The raw address bytes, given as a 32-bits int
 *   @returns       The IP address, as a char pointer (i.e. string)
 *
*/

char *get_IP_address(uint32_t addr) {
    char *result = (char*) malloc(16 * sizeof(char));

    int val1 = (addr >> 24) & 0x0FF;
    int val2 = (addr >> 16) & 0x0FF;
    int val3 = (addr >> 8) & 0x0FF;
    int val4 = (addr & 0x0FF);

    sprintf(result, "%d.%d.%d.%d", val1, val2, val3, val4);
    return result;
}

int main(int argc, char **argv) {
    FILE *f;
    char *source;
    char *destination;
    int first_ihl;
    int first_len;
    int first_offset;
    int total_ip_packets = 0;

    char buff[BUFSIZE];

    if (argc != 2) {
        /* illegal number of arguments */
        printf("Usage : sumary <file>\n");
        return 1; // returning error code 1
    }

    if ((f = fopen(argv[1], "rb")) == 0) {
        /* The given file path is illegal*/
        printf("Could not access the file using the given path");
        return 2; // returning error code 2
    }

    while (!feof(f)) { // reading everything from the given file
        IP *ip = (IP*)malloc(sizeof(IP));
        TCP *tcp = (TCP*)malloc(sizeof(TCP));

        char *curpntr = buff;

        fread(buff, sizeof(char), BUFSIZE, f);
        if (feof(f)) {
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
            source = get_IP_address(ip -> dest);
            destination = get_IP_address(ip -> src);
            first_ihl = hd_len;
            first_len = total_len;
        }

        int rem = (4 * hd_len - BUFSIZE); /*getting the remaining bytes of the header*/
        fseek(f, rem, SEEK_CUR); /*skipping the remainder of the header*/

        fread(buff, sizeof(char), BUFSIZE, f);
        curpntr = buff;
        read_TCP_header(curpntr, tcp);

        int offset = TCP_OFF(tcp -> off);
        if (total_ip_packets == 0) {
            first_offset = offset;
        }

        int rem2 = total_len - (2*BUFSIZE + rem);
        fseek(f, rem2, SEEK_CUR);

        total_ip_packets++;
    }

    printf("%s %s %d %d %d %d\n",
            source, destination, first_ihl, first_len, first_offset, total_ip_packets);

    fclose(f);

    return 0;
}