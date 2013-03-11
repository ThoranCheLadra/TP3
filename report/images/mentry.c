/* Authorship Statement:
Author: Gediminas Leikus
Login (GUID): 1005031l
Title of the assignment: AP3 Exercise 1

This is my own work as defined in the Academic Ethics agreement I have signed.
I used the hashing function from Brian W. Kernighan and Dennis M. Ritchie "The C programming Language" book
*/
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include "mentry.h"
#define MAXADDRESS 500 /* maximum number of characters allowed for full address */
#define MAXSURNAME 100 /* maximum number of characters allowed for surname */
#define MAXPOSTCODE 20 /* maximum number of characters allowed for postcode */

MEntry *me_get(FILE *fd) {
	MEntry *resultP = (MEntry *) malloc(sizeof(MEntry));
	char *full_address = (char *) malloc(sizeof(char) * (MAXADDRESS + 1));
	int i = 0;
	int index = 0;
	int c;
	while ((i != 3) && ((c = fgetc(fd)) != EOF)) {
		if (c == '\n') {
			i = i + 1;
		}
		full_address[index++] = c;	
	}
	full_address[index] = '\0';
	if (i < 2) {
		free(resultP);
		resultP = NULL;
		free(full_address);
		full_address = NULL;
		return NULL; 
		/* tested out, will return segmentation fault 11 if this happens */
	}
	char *surname = (char *) malloc(sizeof(char) * (MAXSURNAME + 1));
	index = 0;
	while ( (surname[index++] = tolower(full_address[index])) != ',' ) {
		/* if the name is in incorrect format, this will give a segmentation fault: 11 as well */
		//strncpy(&(surname[index]), &(full_address[index]), 1);
	}
	surname[index] = '\0';
	int house_number;
	/* go to next line */
	while (full_address[index] != '\n') {
		index = index + 1;
	}
	index = index + 1;

	if (isdigit(full_address[index])) {
		house_number = atoi(&full_address[index]);
	} else {
		house_number = 0;
	}
	while ( full_address[index] != '\n') {
		index = index + 1;
	}
	index = index + 1;	
	char *postcode = malloc(sizeof(char) * (MAXPOSTCODE + 1));
	i = 0;
	while ( (full_address[index] != '\n') && (full_address[index] != '\0') ) {
		if (isalnum(full_address[index])) {
			postcode[i] = tolower(full_address[index]);
			i = i + 1;	
		}
		index = index + 1;
	}
	postcode[i] = '\0';
	resultP->surname = surname;
	resultP->house_number = house_number;
	resultP->postcode = postcode;
	resultP->full_address = full_address;
	return resultP; 
}

unsigned long me_hash(MEntry *me, unsigned long size) {
	// concatinate and find out
	char *me_cat = (char *) malloc(sizeof(char) * (MAXADDRESS + 1));
	strcpy(me_cat, me->surname);
	strcat(me_cat, me->postcode);
	char *me_house_number = (char *) malloc(sizeof(char) * (MAXADDRESS - MAXSURNAME - MAXPOSTCODE + 1));
	//sprintf(me_house_number, "%d", me->house_number);
	strcat(me_cat, me_house_number);
	//fprintf(stderr, "HASH: %s %s %s %d\n", me_cat, me->surname, me->postcode, me->house_number);
	
	unsigned hashval;
	for (hashval = 0; *me_cat != '\0'; me_cat++) {
		hashval = *me_cat + 31 * hashval;
	}

	//free(me_cat);
	//free(me_house_number);
	return hashval % size;
}

/* me_print prints the full address on fd */
void me_print(MEntry *me, FILE *fd) {
	fprintf(fd, "%s", me->full_address);
}

/* me_compare compares two mail entries, returning <0, 0, >0 if
* me1<me2, me1==me2, me1>me2
*/
int me_compare(MEntry *me1, MEntry *me2) {
	// concatinate and find out
	char *me1_cat = (char *) malloc(sizeof(char) * (MAXADDRESS + 1));
	strcpy(me1_cat, me1->surname);
	strcat(me1_cat, me1->postcode);
	char *me1_house_number = (char *) malloc(sizeof(char) * (MAXADDRESS - MAXSURNAME - MAXPOSTCODE + 1));
	sprintf(me1_house_number,"%d", me1->house_number);
	strcat(me1_cat, me1_house_number);
	
	char *me2_cat = malloc(sizeof(char) * (MAXADDRESS + 1));
	strcpy(me2_cat, me2->surname);
	strcat(me2_cat, me2->postcode);
	char *me2_house_number = malloc(sizeof(char) * (MAXADDRESS - MAXSURNAME - MAXPOSTCODE + 1));
	sprintf(me2_house_number, "%d", me2->house_number);
	strcat(me2_cat, me2_house_number);
	
	int result = strcmp(me1_cat, me2_cat);
	free(me1_cat);
	free(me1_house_number);
	free(me2_cat);
	free(me2_house_number);
	return result;
}

/* me_destroy destroys the mail entry
*/
void me_destroy(MEntry *me) {
	//free(me->full_address);
	//free(me->surname);
	//free(me->postcode);
	//free(me);
	me = NULL;
}

