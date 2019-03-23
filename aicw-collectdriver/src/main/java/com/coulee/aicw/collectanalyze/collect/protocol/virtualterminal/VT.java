package com.coulee.aicw.collectanalyze.collect.protocol.virtualterminal;

/**
 * @author liaowufeng
 * @version 1.0
 * 终端VT
 *
 */
@SuppressWarnings("unused")
public class VT {

    private final static char ESC = 27;
	private final static char IND = 132;
    private final static char NEL = 133;
    private final static char RI = 141;
    private final static char SS2 = 142;
    private final static char SS3 = 143;
    private final static char DCS = 144;
    private final static char HTS = 136;
    private final static char CSI = 155;
    private final static char OSC = 157;
    private final static int TSTATE_DATA = 0;
    private final static int TSTATE_ESC = 1; /* ESC */
    private final static int TSTATE_CSI = 2; /* ESC [ */
    private final static int TSTATE_DCS = 3; /* ESC P */
    private final static int TSTATE_DCEQ = 4; /* ESC [? */
    private final static int TSTATE_ESCSQUARE = 5; /* ESC # */
    private final static int TSTATE_OSC = 6; /* ESC ] */
    private final static int TSTATE_SETG0 = 7; /* ESC (? */
    private final static int TSTATE_SETG1 = 8; /* ESC )? */
    private final static int TSTATE_SETG2 = 9; /* ESC *? */
    private final static int TSTATE_SETG3 = 10; /* ESC +? */
    private final static int TSTATE_CSI_DOLLAR = 11; /* ESC [ Pn $ */
    private final static int TSTATE_CSI_EX = 12; /* ESC [ ! */
    private final static int TSTATE_ESCSPACE = 13; /* ESC <space> */
    private final static int TSTATE_VT52X = 14;
    private final static int TSTATE_VT52Y = 15;
    private final static int TSTATE_CSI_TICKS = 16;
    private final static int TSTATE_CSI_EQUAL = 17; /* ESC [ = */

    private int term_state = TSTATE_DATA;

	private final static Byte bCR=new Byte((byte)'\n');
    
    private final static Byte sCR=new Byte((byte)' ');

    private String osc, dcs;

    public VT() {
    }

    public Byte process(char c) {
        switch (term_state) {
        case TSTATE_DATA:
            switch (c) {
            case SS3:
            case SS2:
                break;
            case CSI: // should be in the 8bit section, but some BBS use this
                term_state = TSTATE_CSI;
                break;
            case ESC:
                term_state = TSTATE_ESC;
                break;
            default:
                if (c < 32 && c!='\n' ){
                    break;
                }
                return new Byte((byte) c);
            }
            break;
        case TSTATE_OSC:
            if ((c < 0x20) && (c != ESC)) { // NP - No printing character
                term_state = TSTATE_DATA;
                break;
            }
            if (c == '\\' && osc.charAt(osc.length() - 1) == ESC) {
                term_state = TSTATE_DATA;
                break;
            }
            osc = osc + c;
            break;
        case TSTATE_ESCSPACE:
            term_state = TSTATE_DATA;
            switch (c) {
            case 'F':
                break;
            case 'G': /* S8C1T, Enable output of 8-bit control codes*/
                break;
            default:
                break;
            }
            break;
        case TSTATE_ESC:
            term_state = TSTATE_DATA;
            switch (c) {
            case ' ':
                term_state = TSTATE_ESCSPACE;
                break;
            case '#':
                term_state = TSTATE_ESCSQUARE;
                break;
            case 'c':
                break;
            case '[':
                term_state = TSTATE_CSI;
                break;
            case ']':
                osc = "";
                term_state = TSTATE_OSC;
                break;
            case 'P':
                dcs = "";
                term_state = TSTATE_DCS;
                break;
            case 'A': /* CUU */
                break;
            case 'B': /* CUD */
                break;
            case 'C':
                break;
            case 'I': // RI
                break;
            case 'E': /* NEL */
                break;
            case 'D': /* IND */
                break;
            case 'J': /* erase to end of screen */
                break;
            case 'K':
                break;
            case 'M': // RI
                break;
            case 'H':
                break;
            case 'N': // SS2
                break;
            case 'O': // SS3
                break;
            case '=':
                break;
            case '<': /* vt52 mode off */
                break;
            case '>': /*normal keypad*/
                break;
            case '7': /*save cursor, attributes, margins */
                break;
            case '8': /*restore cursor, attributes, margins */
                break;
            case '(': /* Designate G0 Character set (ISO 2022) */
                term_state = TSTATE_SETG0;
                break;
            case ')': /* Designate G1 character set (ISO 2022) */
                term_state = TSTATE_SETG1;
                break;
            case '*': /* Designate G2 Character set (ISO 2022) */
                term_state = TSTATE_SETG2;
                break;
            case '+': /* Designate G3 Character set (ISO 2022) */
                term_state = TSTATE_SETG3;
                break;
            case '~': /* Locking Shift 1, right */
                break;
            case 'n': /* Locking Shift 2 */
                break;
            case '}': /* Locking Shift 2, right */
                break;
            case 'o': /* Locking Shift 3 */
                break;
            case '|': /* Locking Shift 3, right */
                break;
            case 'Y': /* vt52 cursor address mode , next chars are x,y */
                term_state = TSTATE_VT52Y;
                break;
            default:
                break;
            }
            break;
        case TSTATE_VT52X:
            term_state = TSTATE_VT52Y;
            break;
        case TSTATE_VT52Y:
            term_state = TSTATE_DATA;
            break;
        case TSTATE_SETG0:
            term_state = TSTATE_DATA;
            break;
        case TSTATE_SETG1:
            term_state = TSTATE_DATA;
            break;
        case TSTATE_SETG2:
            term_state = TSTATE_DATA;
            break;
        case TSTATE_SETG3:
            term_state = TSTATE_DATA;
            break;
        case TSTATE_ESCSQUARE:
            switch (c) {
            case '8':
                break;
            default:
                break;
            }
            term_state = TSTATE_DATA;
            break;
        case TSTATE_DCS:
            if (c == '\\' && dcs.charAt(dcs.length() - 1) == ESC) {
                term_state = TSTATE_DATA;
                break;
            }
            dcs = dcs + c;
            break;
        case TSTATE_DCEQ:
            term_state = TSTATE_DATA;
            switch (c) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                term_state = TSTATE_DCEQ;
                break;
            case ';':
                term_state = TSTATE_DCEQ;
                break;
            case 's': // XTERM_SAVE missing!
                break;
            case 'r': // XTERM_RESTORE
                break;
            case 'h': // DECSET
                break;
            case 'i': // DEC Printer Control, autoprint, echo screenchars to printer
                break;
            case 'l': //DECRST
                break;
            case 'n':
                break;
            default:
                break;
            }
            break;
        case TSTATE_CSI_EX:
            term_state = TSTATE_DATA;
            switch (c) {
            case ESC:
                term_state = TSTATE_ESC;
                break;
            default:
                break;
            }
            break;
        case TSTATE_CSI_TICKS:
            term_state = TSTATE_DATA;
            switch (c) {
            case 'p':
                break;
            default:
                break;
            }
            break;
        case TSTATE_CSI_EQUAL:
            term_state = TSTATE_DATA;
            switch (c) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                term_state = TSTATE_CSI_EQUAL;
                break;
            case ';':
                term_state = TSTATE_CSI_EQUAL;
                break;
            case 'F': /* SCO ANSI foreground */
                break;
            case 'G': /* SCO ANSI background */
                break;
            default:
                break;
            }
            break;
        case TSTATE_CSI_DOLLAR:
            term_state = TSTATE_DATA;
            switch (c) {
            case '}':
               break;
            case '~':
                break;
            default:
                break;
            }
            break;
        case TSTATE_CSI:
            term_state = TSTATE_DATA;
            switch (c) {
            case '"':
                term_state = TSTATE_CSI_TICKS;
                break;
            case '$':
                term_state = TSTATE_CSI_DOLLAR;
                break;
            case '=':
                term_state = TSTATE_CSI_EQUAL;
                break;
            case '!':
                term_state = TSTATE_CSI_EX;
                break;
            case '?':
                term_state = TSTATE_DCEQ;
                break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                term_state = TSTATE_CSI;
                break;
            case ';':
                term_state = TSTATE_CSI;
                break;
            case 'c': /* send primary device attributes */
                break;
            case 'q':
                break;
            case 'g':
                break;
            case 'h':
                break;
            case 'i': // Printer Controller mode.
                break;
            case 'l':
                break;
            case 'A': // CUU
                break;
            case 'B': // CUD
                break;
            case 'C':
                break;
            case 'd': // CVA
                break;
            case 'D':
                break;
            case 'r': // DECSTBM
                break;
            case 'G': /* CUP  / cursor absolute column */
                break;
            case 'H': /* CUP  / cursor position */
                break;
            case 'f': /* move cursor 2 */
                break;
            case 'S': /* ind aka 'scroll forward' */
                break;
            case 'L':
                break;
            case 'T': /* 'ri' aka scroll backward */
                break;
            case 'M':
                break;
            case 'K':
                break;
            case 'J':
                break;
            case '@':
                break;
            case 'X':
                break;
            case 'P':
                break;
            case 'n':
                break;
            case 's': /* DECSC - save cursor */
                break;
            case 'u': /* DECRC - restore cursor */
                break;
            case 'm': /* attributes as color, bold , blink,*/
                break;
            default:
                break;
            }
            break;
        default:
            term_state = TSTATE_DATA;
            break;
        }
        return sCR;
    }

}
