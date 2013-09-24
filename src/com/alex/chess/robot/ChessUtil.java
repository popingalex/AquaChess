package com.alex.chess.robot;

import java.util.Stack;

import com.alex.chess.core.ChessConstant;
import com.alex.chess.core.ChessMove;
import com.alex.chess.core.RuleUtil;

public class ChessUtil implements ChessConstant{
    
    public static boolean isChecked(int player, int[] square) { //is going to die
        int i, j, squareSrc, squareDst;
        int pcSelfSide, pcOppSide, pieceDst, nDelta;
        pcSelfSide = RuleUtil.getSide(player);      // get side
        pcOppSide = RuleUtil.getOppsiteSide(player);    // get other side

        for (squareSrc = 0; squareSrc < 256; squareSrc ++) {
            // 0. get my general
            if (square[squareSrc] != pcSelfSide + PIECE_GENERAL) {
                continue;
            }
            // 1. is huntting by Soldier
            if (square[RuleUtil.stepForward(squareSrc, player)] == pcOppSide + PIECE_PAWN) {
                return true;
            }
            for (nDelta = -1; nDelta <= 1; nDelta += 2) {
                if (square[squareSrc + nDelta] == pcOppSide + PIECE_PAWN) {
                    return true;
                }
            }
            // 2. if huntting by Knight
            for (i = 0; i < 4; i ++) {
                if (square[squareSrc + spanGuard[i]] != 0) { // the Knight is pinned
                    continue;
                }
                for (j = 0; j < 2; j ++) {
                    int pieceDstt = square[squareSrc + spanKnightKill[i][j]];
                    if (pieceDstt == pcOppSide + PIECE_HORSE) {
                        return true;
                    }
                }
            }
            // 3. if huntting by Chariot, Cannon or General
            for (i = 0; i < 4; i ++) {
                nDelta = spanGeneral[i];
                squareDst = squareSrc + nDelta;
                while (RuleUtil.inBoard(squareDst)) {
                    pieceDst = square[squareDst];
                    if (pieceDst != 0) {
                        if (pieceDst == pcOppSide + PIECE_CHARIOT || pieceDst == pcOppSide + PIECE_GENERAL) {
                            return true;
                        }
                        break;
                    }
                    squareDst += nDelta;
                }
                squareDst += nDelta;
                while (RuleUtil.inBoard(squareDst)) {
                    pieceDst = square[squareDst];  
                    if (pieceDst != 0) {
                        if (pieceDst == pcOppSide + PIECE_CANNON) {
                            return true;
                        }
                        break;
                    }
                    squareDst += nDelta;
                }
            }
            return false;
        }
        return false;
    }
    
    public static int generateMoves(int player, int[] square, Integer[] mvs) { // generate moves
        int i, j, nGenMoves, nDelta, squareSrc, squareDst;
        int pcSelfSide, pcOppSide, pieceSrc, pieceDst;

        nGenMoves = 0;
        pcSelfSide = RuleUtil.getSide(player);
        pcOppSide = RuleUtil.getOppsiteSide(player);
        for (squareSrc = 0; squareSrc < 256; squareSrc ++) {
            // 1. get a piece of mine
            pieceSrc = square[squareSrc];
            if ((pieceSrc & pcSelfSide) == 0) {
                continue;
            }
            // 2. get able move of piece
            switch (pieceSrc - pcSelfSide) {
            case PIECE_GENERAL:
                for (i = 0; i < 4; i ++) {
                    squareDst = squareSrc + spanGeneral[i];
                    if (!RuleUtil.inFort(squareDst)) {
                        continue;
                    }
                    pieceDst = square[squareDst];
                    if ((pieceDst & pcSelfSide) == 0) {
                        mvs[nGenMoves] = RuleUtil.getMove(squareSrc, squareDst);// make move by src & dst
                        nGenMoves ++;
                    }
                }
                break;
            case PIECE_GUARD:
                for (i = 0; i < 4; i ++) {
                    squareDst = squareSrc + spanGuard[i];  
                    if (!RuleUtil.inFort(squareDst)) {
                        continue;
                    }
                    pieceDst = square[squareDst];
                    if ((pieceDst & pcSelfSide) == 0) {
                        mvs[nGenMoves] = RuleUtil.getMove(squareSrc, squareDst);
                        nGenMoves ++;
                    }
                }
                break;
            case PIECE_ELEPHANT:
                for (i = 0; i < 4; i ++) {
                    squareDst = squareSrc + spanGuard[i];  
                    if (!(RuleUtil.inBoard(squareDst) && RuleUtil.partNative(squareDst, player) && square[squareDst] == 0)) {
                        continue;
                    }
                    squareDst += spanGuard[i];  
                    pieceDst = square[squareDst];
                    if ((pieceDst & pcSelfSide) == 0) {
                        mvs[nGenMoves] = RuleUtil.getMove(squareSrc, squareDst);
                        nGenMoves ++;
                    }
                }
                break;
            case PIECE_HORSE:
                for (i = 0; i < 4; i ++) {
                    squareDst = squareSrc + spanGeneral[i];  
                    if (square[squareDst] != 0) {
                        continue;
                    }
                    for (j = 0; j < 2; j ++) {
                        squareDst = squareSrc + spanKnight[i][j];
                        if (!RuleUtil.inBoard(squareDst)) {  
                            continue;
                        }
                        pieceDst = square[squareDst];
                        if ((pieceDst & pcSelfSide) == 0) {
                            mvs[nGenMoves] = RuleUtil.getMove(squareSrc, squareDst);
                            nGenMoves ++;
                        }
                    }
                }
                break;
            case PIECE_CHARIOT:
                for (i = 0; i < 4; i ++) {
                    nDelta = spanGeneral[i];  
                    squareDst = squareSrc + nDelta;
                    while (RuleUtil.inBoard(squareDst)) {
                        pieceDst = square[squareDst];
                        if (pieceDst == 0) {
                            mvs[nGenMoves] = RuleUtil.getMove(squareSrc, squareDst);
                            nGenMoves ++;
                        } else {
                            if ((pieceDst & pcOppSide) != 0) {
                                mvs[nGenMoves] = RuleUtil.getMove(squareSrc, squareDst);
                                nGenMoves ++;
                            }
                            break;
                        }
                        squareDst += nDelta;
                    }
                }
                break;
            case PIECE_CANNON:
                for (i = 0; i < 4; i ++) {
                    nDelta = spanGeneral[i];
                    squareDst = squareSrc + nDelta;
                    while (RuleUtil.inBoard(squareDst)) {
                        pieceDst = square[squareDst];
                        if (pieceDst == 0) {
                            mvs[nGenMoves] = RuleUtil.getMove(squareSrc, squareDst);
                            nGenMoves ++;
                        } else {
                            break;
                        }
                        squareDst += nDelta;
                    }
                    squareDst += nDelta;
                    while (RuleUtil.inBoard(squareDst)) {
                        pieceDst = square[squareDst];
                        if (pieceDst != 0) {
                            if ((pieceDst & pcOppSide) != 0) {
                                mvs[nGenMoves] = RuleUtil.getMove(squareSrc, squareDst);
                                nGenMoves ++;
                            }
                            break;
                        }
                        squareDst += nDelta;
                    }
                }
                break;
            case PIECE_PAWN:
                squareDst = RuleUtil.stepForward(squareSrc, player);// what the mean? thu result in step1 or -1
                if (RuleUtil.inBoard(squareDst)) {// make sure its legal
                    pieceDst = square[squareDst];
                    if ((pieceDst & pcSelfSide) == 0) {
                        mvs[nGenMoves] = RuleUtil.getMove(squareSrc, squareDst);
                        nGenMoves ++;
                    }
                }
                if (RuleUtil.partNeighbour(squareSrc, player)) {    // is over the river
                    for (nDelta = -1; nDelta <= 1; nDelta += 2) {  //-1 or +1
                        squareDst = squareSrc + nDelta;
                        if (RuleUtil.inBoard(squareDst)) {
                            pieceDst = square[squareDst];
                            if ((pieceDst & pcSelfSide) == 0) {
                                mvs[nGenMoves] = RuleUtil.getMove(squareSrc, squareDst);
                                nGenMoves ++;
                            }
                        }
                    }
                }
                break;
            }
        }
        return nGenMoves;
    }
   
    public static int sort(int src, int[] square) {
        int x = RuleUtil.getX(src);
        int y = RuleUtil.getY(src);
        int piece = square[src];
        int count = 0;
        int index = 0;
        for(int i=3;i<13;i++) {
            if(piece==square[16*i+x]) count++;
            if(i==y) index = count;
        }
        if(piece-15>0)
            index = count+1-index;
        if (count == 1) return 0;
        else if (index == 1)
            return 6;
        else if (count < 4) {
            return 8-(count-index); //6 means after, 7 means mid
        }else
            return index;   //return 2, 3, 4, 5
    }   

    public static boolean checkRepeat(int move, Stack<ChessMove> stack) {
        return ( stack.size()>12 && (
                move == stack.get(stack.size()-4).move) && 
                stack.get(stack.size()-1).move == stack.get(stack.size()-5).move && 
                stack.get(stack.size()-2).move == stack.get(stack.size()-6).move && 
                stack.get(stack.size()-3).move == stack.get(stack.size()-7).move && 
                stack.get(stack.size()-4).move == stack.get(stack.size()-8).move && 
                stack.get(stack.size()-5).move == stack.get(stack.size()-9).move && 
                stack.get(stack.size()-6).move == stack.get(stack.size()-10).move && 
                stack.get(stack.size()-7).move == stack.get(stack.size()-11).move && 
                stack.get(stack.size()-8).move == stack.get(stack.size()-12).move );
    }
    
    public static boolean checkLegal(int player, int move, int[] square) { //judge a move is legal
        int selfSide, pieceSrc, pieceDst, nDelta;
        int squareSrc = RuleUtil.getSrc(move);
        pieceSrc = square[squareSrc];
        selfSide = RuleUtil.getSide(player);  // get side
        if ((pieceSrc & selfSide) == 0) { //src is not mine
            return false;
        }
        int squareDst = RuleUtil.getDst(move);
        pieceDst = square[squareDst];
        if ((pieceDst & selfSide) != 0) { //dst is mine
            return false;
        }
        int pin;
        switch (pieceSrc - selfSide) {
        case PIECE_GENERAL:
            return RuleUtil.inFort(squareDst) && RuleUtil.spanKing(squareSrc, squareDst);  
        case PIECE_GUARD:
            return RuleUtil.inFort(squareDst) && RuleUtil.spanAdvisor(squareSrc, squareDst);
        case PIECE_ELEPHANT:
            return RuleUtil.partSame(squareSrc, squareDst) && RuleUtil.spanElephant(squareSrc, squareDst) &&
                    square[RuleUtil.pinElephant(squareSrc, squareDst)] == 0;
        case PIECE_HORSE:
            pin = RuleUtil.pinHorse(squareSrc, squareDst);
            return pin != squareSrc && square[pin] == 0;
        case PIECE_CHARIOT:
        case PIECE_CANNON:
            if (RuleUtil.rowSame(squareSrc, squareDst)) {
                nDelta = (squareDst < squareSrc ? -1 : 1);
            } else if (RuleUtil.colSame(squareSrc, squareDst)) {
                nDelta = (squareDst < squareSrc ? -16 : 16);
            } else {
                return false;
            }
            pin = squareSrc + nDelta;
            while (pin != squareDst && square[pin] == 0) {
                pin += nDelta;
            }
            if (pin == squareDst) {
                return pieceDst == 0 || pieceSrc - selfSide == PIECE_CHARIOT;
            } else if (pieceDst != 0 && pieceSrc - selfSide == PIECE_CANNON) {
                pin += nDelta;
                while (pin != squareDst && square[pin] == 0) {
                    pin += nDelta;
                }
                return pin == squareDst;
            } else {
                return false;
            }
        case PIECE_PAWN:
            if (RuleUtil.partNeighbour(squareDst, player) && (squareDst == squareSrc - 1 || squareDst == squareSrc + 1)) { // over river then left / right
                return true;
            }
            return squareDst == RuleUtil.stepForward(squareSrc, player);  // or anywhere forward
        default:
            return false;
        }
    }
}
