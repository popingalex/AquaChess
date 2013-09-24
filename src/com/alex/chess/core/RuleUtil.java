package com.alex.chess.core;


public class RuleUtil implements ChessConstant{
	/**
	 * @param location
	 * @return if piece in board
	 */
	public static boolean inBoard(int loc) {
		return areaBoard[loc] != 0;
	}
	/**
	 * @param loc
	 * @return if piece in fort
	 */
	public static boolean inFort(int loc) {
		return areaFort[loc] != 0;
	}
	/**
	 * @param loc
	 * @return y of piece (rank)
	 */
	public static int getY(int loc) {
		return loc >> 4;
	}
	/**
	 * @param loc
	 * @return x of piece (file)
	 */
	public static int getX(int loc) {
		return loc & 15;
	}
	/**
	 * @param x
	 * @param y
	 * @return location of piece
	 */
	public static int getLocation(int x, int y) {
		return x + (y << 4);
	}
	/**
	 * @param loc
	 * @return piece loaction of other side
	 */
	public static int reverseSide(int loc) {
		return 254 - loc;
	}
	/**
	 * @param x
	 * @return horizontal flipped x
	 */
	public static int flipX(int x) {
		return 14 - x;
	}
	/**
	 * @param y
	 * @return vertical flipped y
	 */
	public static int flipY(int y) {
		return 15 - y;
	}
	/**
	 * @param loc
	 * @return flipped location
	 */
	public static int flipLocation(int loc) {
		return getLocation(flipX(getX(loc)), getY(loc));
	}
	/**
	 * @param loc
	 * @param side
	 * @return step forward
	 */
	public static int stepForward(int loc, int side) {
		return loc - 16 + (side << 5);
	}
	/**
	 * @param src
	 * @param dst
	 * @return move is legal for General
	 */
	public static boolean spanKing(int src, int dst) {
		return spanRoyal[dst - src + 256] == 1;
	}
	/**
	 * @param src
	 * @param dst
	 * @return move is legal for Advisor
	 */
	public static boolean spanAdvisor(int src, int dst) {
		return spanRoyal[dst - src + 256] == 2;
	}
	/**
	 * @param src
	 * @param dst
	 * @return move is legal for Elephant
	 */
	public static boolean spanElephant(int src, int dst) {
		return spanRoyal[dst - src + 256] == 3;
	}
	/**
	 * @param src
	 * @param dst
	 * @return pin of Elephant
	 */
	public static int pinElephant(int src, int dst) {
		return (src + dst) >> 1;
	}
	/**
	 * @param src
	 * @param dst
	 * @return pin of Horse
	 */
	public static int pinHorse(int src, int dst) {
		return src + pinHorse[dst - src + 256];
	}
	/**
	 * @param loc
	 * @param side
	 * @return is Native
	 */
	public static boolean partNative(int loc, int side) {
		return (loc & 0x80) != (side << 7);
	}
	/**
	 * @param loc
	 * @param side
	 * @return is Not Native
	 */
	public static boolean partNeighbour(int loc, int side) {
		return (loc & 0x80) == (side << 7);
	}
	/**
	 * @param src
	 * @param dst
	 * @return is at Same Part
	 */
	public static boolean partSame(int src, int dst) {
		return ((src ^ dst) & 0x80) == 0;
	}
	/**
	 * @param src
	 * @param dst
	 * @return is at same Row
	 */
	public static boolean rowSame(int src, int dst) {
		return ((src ^ dst) & 0xf0) == 0;
	}
	/**
	 * @param src
	 * @param dst
	 * @return is at same Column
	 */
	public static boolean colSame(int src, int dst) {
		return ((src ^ dst) & 0x0f) == 0;
	}
	/**
	 * @param player
	 * @return side of piece
	 */
	public static int getSide(int player) {
		return 8 + (player << 3);
	}
	/**
	 * @param player
	 * @return another side of piece
	 */
	public static int getOppsiteSide(int player) {
		return 16 - (player << 3);
	}
	/**
	 * @param mv
	 * @return SRC of Move
	 */
	public static int getSrc(int mv) {
		return mv & 255;  // get low 11111111
	}
	/**
	 * @param mv
	 * @return DST of Move
	 */
	public static int getDst(int mv) {
		return mv >> 8;   //get high 11111111
	}
	/**
	 * @param src
	 * @param dst
	 * @return Move made by src & dst
	 */
	public static int getMove(int src, int dst) {
		return src + dst * 256;
	}
	/**
	 * @param mv
	 * @return mirror of Move
	 */
	public static int getMirrorMove(int move) {
		return getMove(flipLocation(getSrc(move)), flipLocation(getDst(move)));
	}
}
