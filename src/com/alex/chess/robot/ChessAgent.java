package com.alex.chess.robot;

import java.util.Arrays;
import java.util.Comparator;

import com.alex.chess.core.ChessAdapter;
import com.alex.chess.core.ChessMoveNode;
import com.alex.chess.core.RuleUtil;

public class ChessAgent extends ChessAdapter{
    private static ChessAgent instance;
    private ChessAgent() {
        /*JNIUtil util = new JNIUtil();
        
        util.init();
        System.out.println(util.get());*/
    }
    public static ChessAgent getInstance() {
        if(null==instance)
            instance = new ChessAgent();
        return instance;
    }

    public boolean makeMove(int move, int pcCaptured) {
        pcCaptured = movePiece(move);  
        if (ChessUtil.isChecked(player, square)) {
            undoMove(move, pcCaptured);
            return false;
        }
        changeSide();
        distance ++;
        return true;
    }
    public int searchWork(int depth, int limit) { //iterative deepening search
        int  vl;
        // init
        for(int i=0;i<historyTable.length;i++)
        {
            historyTable[i]=0;
        }
        distance = 0; // current depth
        long start = System.currentTimeMillis();
        long end = 0;
        int i;
        // process
        for (i = depth; i <= depth; i ++) {   
            vl = searchFull(-MATE_VALUE, MATE_VALUE, i, root);  
            end = System.currentTimeMillis();
            if (vl > WIN_VALUE || vl < -WIN_VALUE) {// no mean to continue
                break;
            }
            if((end-start)>limit) {// time's up
                //                System.out.println("depth:"+i);
                break;//spent to much time thinking
            }
        }
        if(end-start < delay) {
            try {
                Thread.sleep(delay - (end-start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return bestMove;
    }
    private long delay = 500;
    /**
     * (Fail-Soft) Alpha-Beta
     * @param valueAlpha
     * @param valueBeta
     * @param depth
     * @param history 
     * @return
     */
    public int searchFull(int valueAlpha, int valueBeta, int depth, ChessMoveNode root) {
        int i=0, nGenMoves=0,captured=0;
        int value, valueBest, moveBest;
        Integer moves[]=new Integer[MAX_GEN_MOVES];
        if (depth == 0) {
            return evaluate();
        }
        valueBest = -MATE_VALUE; // able to know if no move made
        moveBest = 0;           // able to know if reach Beta or PV, for save to history
        nGenMoves = ChessUtil.generateMoves(player, square, moves);
        Arrays.sort(moves, 0,nGenMoves, moveComparator);
        // 4. do these moves
        for (i = 0; i < nGenMoves; i ++) {    
            captured=square[RuleUtil.getDst(moves[i])];
            //int src = RuleUtil.getSrc(moves[i]);
            //int piece = square[src];
            //ChessMoveNode node = new ChessMoveNode(moves[i], captured, piece, ChessUtil.sort(src, square), depth);
            if (makeMove(moves[i], captured)) {
                value = -searchFull(-valueBeta, -valueAlpha, depth - 1, null);
                //root.childs.add(node);
                //node.parent = root;
                distance --;
                changeSide();
                undoMove(moves[i], captured);    //
                if (value > valueBest) {		// find the best (not sure it's Alpha, PV or Beta)
                    valueBest = value;		// "vlBest" is the best to return, may beyond Alpha-Beta 
                    if (value >= valueBeta) {	// find a Beta
                        moveBest = moves[i];// return the best
                        {
                            break;			// Beta drop
                        }
                    } else if (value > valueAlpha) {	// find a PV
                        moveBest = moves[i];// save PV to history
                        valueAlpha = value;	// shrink Alpha-Beta
                    }
                }
            }
        }
        // 5. all moves over, return the best, save to history if its not Alpha
        if (valueBest == -MATE_VALUE) {
            // give a value if is a kill
            return distance - MATE_VALUE;
        }
        if (moveBest != 0) {
            // save it if its not Alpha
            historyTable[moveBest] += depth * depth;
            if (distance == 0) {
                // save the root move
                bestMove = moveBest;
            }
        }
        return valueBest;
    }
    private Comparator<Integer> moveComparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            int i1 = o1;
            int i2 = o2;
            return historyTable[i2]-historyTable[i1];
        }
    };
}
