package com.alex.chess;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.alex.aqua.bundle.EchoBundle;
import com.alex.aqua.core.Aqua;
import com.alex.aqua.framework.impl.ViewAdapter;
import com.alex.chess.core.ChessConstant;
import com.alex.chess.core.ChessMove;
import com.alex.chess.core.ChessMoveNode;
import com.alex.chess.util.ShapeUtil;
import com.alex.chess.widget.DecorButton;
import com.alex.chess.widget.PieceLabel;
import com.alex.chess.widget.SwitchButton;

public class ViewGame extends ViewAdapter implements Constant, ChessConstant{
	@Override
	public int handleEcho(EchoBundle source, EchoBundle result) {
		super.handleEcho(source, result);
		switch (source.param) {
		case CHESS_RESULT_ROBOTOVER:
			submit(0, EchoBundle.extract(CHESS_REQUEST_ROBOTMOVE, null), null);
			break;
		case CHESS_RESULT_MATED:
			JOptionPane.showMessageDialog(null, "Lose");
			buttonStart.doClick();
			submit(0, EchoBundle.extract(CHESS_INFO_SET, null), null);
			break;
		case CHESS_REQUEST_FRESHSQURE:
			stateBundle = (StateBundle) source.bundle;
			playerSwitchRed.setSelected(stateBundle.player[0]==PLAYER_HUMAN);
			playerSwitchBlack.setSelected(stateBundle.player[1]==PLAYER_HUMAN);
			for(int i=0;i<256;i++) {
				pieceMap[i]=stateBundle.squre[i];
			}
			for(int j=0;j<10;j++)
			{
				for(int i=0;i<9;i++)
				{
					if(pieceMap[(j+3)*16+i+3]!=0)
					{
						int id = pieceMap[(j+3)*16+i+3]-8;
						if(null != chessMap[j][i]) {
							if(chessMap[j][i].getType() == id) {
								continue;
							} else {
								dropComponent(chessMap[j][i]);
								chessMap[j][i] = null;
							}
						}
						chessMap[j][i] = new PieceLabel(id, radius, mouseAdapter);
						replacePiece(i, j);
						addComponent(chessMap[j][i], 1);
					} else if (null != chessMap[j][i] ) {
						dropComponent(chessMap[j][i]);
						chessMap[j][i] = null;
					}
				}
			}
			if(root!=null) {
				root.removeAllChildren();
				addNode(stateBundle.root, root);
			}
			Stack<ChessMove> history = stateBundle.stack;
			int historySize = history.size();
			String[] histories = new String[historySize];
			for(int i=0;i<historySize;i++) {
				histories[i] = (i+1)+":"+history.get(i).getManual();
			}
			historyList.setListData(histories);
			historyList.ensureIndexIsVisible(historySize-1);
			break;
		default:
			break;
		}
		return 0;
	}
	private void addNode (ChessMoveNode node, DefaultMutableTreeNode tree) {
		for(int i=0;i<node.childs.size();i++) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(node.childs.get(i).getManual());
			tree.add(child);
			addNode(node.childs.get(i), child);
		}
	}
	private PieceLabel[][] chessMap = new PieceLabel[10][9];
	private int pieceMap[]=new int[256];
	private StateBundle stateBundle;
	private int depthLimitMax = 5;
	private int timeLimitMax = 50;
	@Override
	public void init() {
		//============================
	}
	private DecorButton buttonOption;
	private DecorButton buttonStart;
	private DecorButton buttonStep;
	private DecorButton buttonManual;
	private DecorButton buttonRetract;
	private DecorButton buttonSurrender;
	private DecorButton buttonExit;
	private SwitchButton playerSwitchRed;
	private SwitchButton playerSwitchBlack;
	private int featureLeft = 105;
	@Override
	public void beforeDisplay() {
		int squareWidth = 400;
		int squareHeight = 480;
		shapeBoard(0, 0, squareWidth, squareHeight, getWidth(), getHeight(), 20, 2, 5);
		int bWidth = 90;
		int bHeight = 30;
		int fontSize1 = 16;
		int fontSize2 = 14;

		buttonOption = new DecorButton(featureButtonTitles[0], featureButtonCommands[0], bWidth, bHeight, fontSize1);
		buttonStart = new DecorButton(routeButtonTitles[0], routeButtonCommands[0], bWidth, bHeight, fontSize1);
		buttonStep = new DecorButton(routeButtonTitles[2], routeButtonCommands[2], bWidth, bHeight, fontSize1);
		buttonManual = new DecorButton(chessButtonTitles[0], chessButtonCommands[0], bWidth, bHeight, fontSize1);
		{
			buttonManual.setEnabled(false);
		}
		buttonRetract = new DecorButton(chessButtonTitles[1], chessButtonCommands[1], bWidth, bHeight, fontSize1);
		buttonSurrender = new DecorButton(chessButtonTitles[2], chessButtonCommands[2], bWidth, bHeight, fontSize1);
		buttonExit = new DecorButton(featureButtonTitles[1], featureButtonCommands[1], bWidth, bHeight, fontSize1);
		buttonOption.addActionListener(buttonClickListener);
		buttonStart.addActionListener(buttonClickListener);
		buttonStep.addActionListener(buttonClickListener);
		buttonRetract.addActionListener(buttonClickListener);
		buttonSurrender.addActionListener(buttonClickListener);
		buttonExit.addActionListener(buttonClickListener);
		buttonOption.setLocation(squareWidth+featureLeft,    15);
		buttonStart.setLocation(squareWidth+featureLeft,     160-5+(bHeight+5)*0);
		buttonStep.setLocation(squareWidth+featureLeft,      160-5+(bHeight+5)*1);
		buttonManual.setLocation(squareWidth+featureLeft,    160-5+(bHeight+5)*2);
		buttonRetract.setLocation(squareWidth+featureLeft,   160-5+(bHeight+5)*3);
		buttonSurrender.setLocation(squareWidth+featureLeft, 160-5+(bHeight+5)*4);
		buttonExit.setLocation(squareWidth+featureLeft,      480-45);
		addComponent(buttonOption, 1);
		addComponent(buttonStart, 1);
		addComponent(buttonStep, 1);
		//addComponent(buttonManual, 1);
		addComponent(buttonRetract, 1);
		addComponent(buttonSurrender, 1);
		addComponent(buttonExit, 1);

		playerSwitchRed = new SwitchButton(playerButtonTitles[0], playerButtonTitles[1], playerButtonCommands[0], bWidth, bHeight, fontSize2);
		playerSwitchBlack = new SwitchButton(playerButtonTitles[0], playerButtonTitles[1], playerButtonCommands[1], bWidth, bHeight, fontSize2);
		playerSwitchRed.addActionListener(buttonClickListener);
		playerSwitchBlack.addActionListener(buttonClickListener);
		playerSwitchRed.setLocation(squareWidth+featureLeft, 160 + 160 + 10);
		playerSwitchBlack.setLocation(squareWidth+featureLeft, 160 - 40);
		addComponent(playerSwitchRed, 1);
		addComponent(playerSwitchBlack, 1);

		{
			submit(0, EchoBundle.extract(CHESS_INFO_SET, null), null);
			EchoBundle result = EchoBundle.extract();
			submit(0, EchoBundle.extract(CHESS_INFO_GET, null), result);
			stateBundle = (StateBundle) result.bundle;
		}

		depthLimitLabel = new JLabel(scrollbarTitles[0]);
		timeLimitLabel = new JLabel(scrollbarTitles[1]);
		depthScroll = new JScrollBar(JScrollBar.HORIZONTAL, 0, 1, 0, depthLimitMax);
		timeScroll = new JScrollBar(JScrollBar.HORIZONTAL, 0, 1, 0, timeLimitMax);
		depthScroll.setPreferredSize(new Dimension(90, 20));
		timeScroll.setPreferredSize(new Dimension(90, 20));
		depthScroll.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				depthLimitLabel.setText(scrollbarTitles[0]+"["+(e.getValue()+1)+"]");
			}
		});
		timeScroll.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				timeLimitLabel.setText(scrollbarTitles[1]+"["+(e.getValue()+1)*100+"]");
			}
		});

		buttonReset = new DecorButton(featureButtonTitles[2], featureButtonCommands[2], bWidth, bHeight, fontSize1);
		buttonBack = new DecorButton(featureButtonTitles[3], featureButtonCommands[3], bWidth, bHeight, fontSize1);
		buttonReset.addActionListener(buttonClickListener);
		buttonBack.addActionListener(buttonClickListener);

		Rectangle maximumBound = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int dialogWidth = 150;
		int dialogHeight = 200;
		configDialog = new JDialog(MainDemo.frame, true) {
			private static final long serialVersionUID = 1L;
			int edge = 5;
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.setColor(canvasbgColor);
				g.fillRect(0, 0, edge, getHeight());
				g.fillRect(getWidth()-edge, 0, edge, getHeight());
				g.fillRect(edge, 0, getWidth()-edge*2, edge);
				g.fillRect(edge, getHeight()-edge, getWidth()-edge*2, edge);
			}
		};
		configDialog.setLayout(new FlowLayout());
		configDialog.setResizable(false);
		configDialog.setUndecorated(true);
		configDialog.setBounds((maximumBound.width-dialogWidth)/2, (maximumBound.height-dialogHeight)/2, dialogWidth, dialogHeight);
		JLabel gap = new JLabel();
		gap.setPreferredSize(new Dimension(90, 15));
		configDialog.add(gap);
		configDialog.add(depthLimitLabel);
		configDialog.add(depthScroll);
		configDialog.add(timeLimitLabel);
		configDialog.add(timeScroll);
		configDialog.add(buttonReset);
		configDialog.add(buttonBack);

		root = new DefaultMutableTreeNode();
		tree = new JTree(root);

		root.isRoot();
		manualScroll = new JScrollPane(tree);

		historyList.setBackground(historybgColor);
		historyScroll = new JScrollPane(historyList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		historyScroll.setBounds(squareWidth, edge, 100, getHeight()-edge*2);
		addComponent(historyScroll, 1);
	}

	private Color historybgColor = new Color(159, 177, 175);
	private Color canvasbgColor = new Color(86, 106, 118);
	private Color chessbgColor = new Color(178, 186, 149);
	private JList historyList = new JList();
	private JTree tree;

	private JLabel timeLimitLabel;
	private JLabel depthLimitLabel;
	private JScrollBar timeScroll;
	private JScrollBar depthScroll;
	private DecorButton buttonReset;
	private DecorButton buttonBack;
	//private JWindow manualWindow;
	private JDialog configDialog;
	private JScrollPane manualScroll;
	private JScrollPane historyScroll;
	private DefaultMutableTreeNode root;

	private static String[] scrollbarTitles = new String[]{"搜索深度", "搜索限時"};
	private static String[] chessButtonTitles = new String[]{"棋谱", "悔棋", "认输"};
	private static String[] routeButtonTitles = new String[]{"开始", "暂停", "单步"};
	private static String[] playerButtonTitles = new String[]{"玩家", "電脳"};
	private static String[] featureButtonTitles = new String[]{"设置", "退出", "重置", "返回"};
	private static String[] chessButtonCommands = new String[]{"manual", "retract", "surrender"};
	private static String[] routeButtonCommands = new String[]{"start", "pause", "step"};
	private static String[] playerButtonCommands = new String[]{"red", "black"};
	private static String[] featureButtonCommands = new String[]{"config", "exit", "reset", "back"};
	private ActionListener buttonClickListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			if(event.getActionCommand().equals(chessButtonCommands[0])) {           //manual
				manualScroll.setVisible(true);
			} else if(event.getActionCommand().equals(chessButtonCommands[1])) {	//retract
				submit(0, EchoBundle.extract(CHESS_REQUEST_UNMOVE, null), null);
			} else if (event.getActionCommand().equals(chessButtonCommands[2])) {	//surrender
				int option = JOptionPane.showConfirmDialog(null, "Surrender?", "Confirm", JOptionPane.YES_NO_OPTION);
				if(option==JOptionPane.YES_OPTION) {
					buttonStart.doClick();
					submit(0, EchoBundle.extract(CHESS_INFO_SET, null), null);
				}
			} else if (event.getActionCommand().equals(featureButtonCommands[0])) {	//config
				timeScroll.setValue(stateBundle.timeLimit-1);
				depthScroll.setValue(stateBundle.depthLimit-1);
				configDialog.setVisible(true);
			} else if (event.getActionCommand().equals(featureButtonCommands[1])) {	//exit
				Aqua.exitAqua(0);
			} else if (event.getActionCommand().equals(featureButtonCommands[2])) {	//reset
				timeScroll.setValue(stateBundle.timeLimit-1);
				depthScroll.setValue(stateBundle.depthLimit-1);
			} else if (event.getActionCommand().equals(featureButtonCommands[3])) {	//back
				configDialog.setVisible(false);
				stateBundle.depthLimit = depthScroll.getValue()+1;
				stateBundle.timeLimit = timeScroll.getValue()+1;
			} else if (event.getActionCommand().equals(routeButtonCommands[0])) {	//start
				buttonStart.setText(routeButtonTitles[1]);
				buttonStart.setActionCommand(routeButtonCommands[1]);
				submit(0, EchoBundle.extract(CHESS_REQUEST_PLAY, null), null);
			} else if (event.getActionCommand().equals(routeButtonCommands[1])) {
				buttonStart.setText(routeButtonTitles[0]);
				buttonStart.setActionCommand(routeButtonCommands[0]);
				submit(0, EchoBundle.extract(CHESS_REQUEST_PAUSE, null), null);
			} else if (event.getActionCommand().equals(routeButtonCommands[2])) {
				submit(0, EchoBundle.extract(CHESS_REQUEST_STEP, null), null);
			} else if (event.getActionCommand().equals(playerButtonCommands[0])) {
				submit(0, EchoBundle.extract(CHESS_REQUEST_SWITCHP, PLAYER_RED), null);
			} else if (event.getActionCommand().equals(playerButtonCommands[1])) {
				submit(0, EchoBundle.extract(CHESS_REQUEST_SWITCHP, PLAYER_BLACK), null);
			}
		}
	};
	private void replacePiece(int x, int y) {
		if(null==chessMap[y][x]) return; 
		chessMap[y][x].setLocation(marginLeft - radius + (radius*2+pad)*x, marginTop - radius + (radius*2+pad)*y);
	}

	private MouseAdapter mouseAdapter = new MouseAdapter() {
		private int lastX;
		private int lastY;
		private int startX;
		private int startY;
		private Point startPoint;
		private Point restrictedPoint(int x, int y) {
			int offX = x%size;
			int offY = y%size;
			int col = (x - offX)/size;
			int row = (y - offY)/size;
			if(0>col || 0>row || 8<col || 9<row)
				return null;
			return new Point(col, row);
		}
		@Override
		public void mousePressed(MouseEvent e) {
			if(stateBundle.player[stateBundle.current]==PLAYER_ROBOT)return;
			PieceLabel piece = (PieceLabel) e.getComponent();
			layerComponent(piece, 2);
			startPoint = restrictedPoint(piece.getX()+size-marginLeft, piece.getY()+size-marginTop);
			//start point
			startX = e.getComponent().getX();
			startY = e.getComponent().getY();
			lastX = e.getXOnScreen();
			lastY = e.getYOnScreen();
			super.mousePressed(e);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if(stateBundle.player[stateBundle.current]==PLAYER_ROBOT)return;
			PieceLabel piece = (PieceLabel) e.getComponent();
			Point point = restrictedPoint(piece.getX()+size-marginLeft, piece.getY()+size-marginTop);
			if(null == point) {
				piece.setLocation(startX, startY);
			} else {
				layerComponent(piece, 1);	//place piece
				int src = (startPoint.y+3)*16 + startPoint.x+3;
				int move = src + ((point.y+3)*16 + point.x+3)*256;
				EchoBundle result = EchoBundle.extract(0, null);
				submit(0, EchoBundle.extract(CHESS_REQUEST_HUMANMOVE, move), result);
				switch (result.param) {
				case CHESS_RESULT_MATED:
					break;
				case CHESS_RESULT_LEGAL:
					if(stateBundle.state==State.play) {
						buttonStep.doClick();
					}
					break;
				case CHESS_RESULT_CHECK:
					submit(0, EchoBundle.extract(CHESS_REQUEST_STEP, null), null);
					break;
				case CHESS_RESULT_REPEAT:
					piece.setLocation(startX, startY);
					break;
				case CHESS_RESULT_ILLEGAL:
					piece.setLocation(startX, startY);
					break;
				case CHESS_RESULT_SUICIDE:
					piece.setLocation(startX, startY);
					break;
				}
			}
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if(stateBundle.player[stateBundle.current]==PLAYER_ROBOT)return;
			PieceLabel piece = (PieceLabel) e.getComponent();
			//piece.set
			piece.setLocation(piece.getX() + e.getXOnScreen()-lastX, piece.getY() + e.getYOnScreen()-lastY);
			lastX = e.getXOnScreen();
			lastY = e.getYOnScreen();
		}
	};
	@Override
	public void processCanvasPaint(long timestamp, Graphics2D graph) {
		graph.setColor(canvasbgColor );
		graph.fill(canvasBack);
		graph.setColor(chessbgColor);
		{
			//graph.setColor(Color.white);
		}
		graph.fill(chessBack);
		graph.setColor(chessbgColor);
		graph.fill(featureBack);

		graph.setColor(Color.darkGray);
		graph.fill(chessRiver);
		graph.setColor(Color.black);
		graph.draw(chessEdge);
		graph.draw(chessBoard);
		
		//        graph.drawRect(400, 160, 200, 160);

		graph.drawRect(400+featureLeft, 10+30+10, 90, 60);
		graph.drawRect(400+featureLeft, 320+10+30+5, 90, 60);
		class Tiger{};
		new Tiger();
		switch (stateBundle.current) {
		case 1:
			graph.setColor(Color.black);
			graph.fillRect(400+featureLeft, 10+30+10, 90, 60);
			break;
		case 0:
			graph.setColor(Color.red.darker());
			graph.fillRect(400+featureLeft, 320+10+30+5, 90, 60);
			break;
		}
		super.processCanvasPaint(timestamp, graph);
	}
	private int marginLeft;
	private int marginTop;
	private int boardWidth;
	private int boardHeight;
	private int pad;
	private int size;
	private int edge;
	private int radius;
	private Shape canvasBack;
	private Shape chessEdge;
	private Shape chessBoard;
	private Shape chessBack;
	private Shape chessRiver;
	private Shape featureBack;
	//	Shape 
	private void shapeBoard(int left, int top, int squareWidth, int squareHeight, int width, int height, int radius, int pad, int edge) {
		boardWidth = radius*9*2 + pad*8 - radius*2;
		boardHeight = radius*10*2 + pad*8 - radius*2;
		marginLeft = left + (squareWidth - boardWidth)/2;
		marginTop = top + (squareHeight - boardHeight)/2;
		this.pad = pad;
		this.edge = edge;
		this.radius = radius;
		this.size = radius*2+pad;
		
		canvasBack = ShapeUtil.getCanvasBack(left, top, width, height, edge);
		chessEdge = ShapeUtil.getEdgeShape(marginLeft, marginTop, boardWidth, boardHeight, edge);
		chessBoard = ShapeUtil.getBoardShape(marginLeft, marginTop, boardWidth, boardHeight, radius, pad, edge);
		chessBack = ShapeUtil.getChessBack(left, top, width, height, edge);
		chessRiver = ShapeUtil.getChessRiver(marginLeft, marginTop, boardWidth, boardHeight);
		featureBack = ShapeUtil.getFeatureBack(left, top, width, height, edge);
	}
}
