package QLearningArpege;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphPlot extends JPanel{
    private static double MAX_SCORE = 1;
    private static final int PREF_W = 1000;
    private static final int PREF_H = 600;
    private static final int BORDER_GAP = 50;
    private static final Color[] GRAPH_COLOR = new Color[]{Color.green, Color.BLUE, Color.ORANGE, Color.RED, Color.cyan};
    private static final Color[] GRAPH_POINT_COLOR = new Color[]{new Color(90, 212, 25, 228),new Color(25, 72, 212, 228),new Color(177, 83, 11, 228),new Color(236, 18, 36, 228),new Color(66, 219, 188, 228) };
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private static final int GRAPH_POINT_WIDTH = 1;
    private static final int Y_HATCH_CNT = 40;
    private List<List<Double>> scores;

    public GraphPlot(List<List<Double>> scores) {
        this.scores = scores;
    }

    private static void getMaxScore(List<List<Double>> scores){
        double max = 0;
        for(List<Double> scoreList: scores){
            for(Double score: scoreList)
            if (score>max)max = score;
        }
        MAX_SCORE = max + 0.3*max;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        getMaxScore(scores);
        double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (scores.get(0).size() - 1);
        double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE);

        List<List<Point>> graphPoints = new ArrayList<>();
        for(List<Double> scoresList: scores) {
            List<Point> graphPointList = new ArrayList<>();
            for (int i = 0; i < scoresList.size(); i++) {
                int x1 = (int) (i * xScale + BORDER_GAP);
                int y1 = (int) ((MAX_SCORE - scoresList.get(i)) * yScale + BORDER_GAP);
                graphPointList.add(new Point(x1, y1));
            }
            graphPoints.add(graphPointList);
        }

        // create x and y axes
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

        FontMetrics fm = g2.getFontMetrics();
        // create hatch marks for y axis.
        for (int i = 0; i < Y_HATCH_CNT; i++) {
            int x0 = BORDER_GAP;
            int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
            int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
            int y1 = y0;
            g2.drawLine(x0, y0, x1, y1);
//            double x =((MAX_SCORE + BORDER_GAP - (double)y1)/ yScale);
//            String value = Double.toString(Math.round(x*10000.0)/10000.0);
//            g2.drawString(value, x0 - fm.stringWidth(value), y0 + (fm.getAscent() / 2));
        }

        // and for x axis
        for (int i = 0; i < scores.get(0).size() - 1; i++) {
            int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (scores.get(0).size() - 1) + BORDER_GAP;
            int x1 = x0;
            int y0 = getHeight() - BORDER_GAP;
            int y1 = y0 - GRAPH_POINT_WIDTH;
            g2.drawLine(x0, y0, x1, y1);
            if(i%(scores.get(0).size()/15)==0) {
                String value = Integer.toString(i);
                g2.drawString(value, x0 - fm.stringWidth(value), y0 + (fm.getAscent()));
            }
        }

        Stroke oldStroke = g2.getStroke();
        g2.setStroke(GRAPH_STROKE);
        for(List<Point> graphPointList: graphPoints) {
            g2.setColor(GRAPH_COLOR[graphPoints.indexOf(graphPointList)]);
            for (int i = 0; i < graphPointList.size() - 1; i++) {
                int x1 = graphPointList.get(i).x;
                int y1 = graphPointList.get(i).y;
                int x2 = graphPointList.get(i + 1).x;
                int y2 = graphPointList.get(i + 1).y;
                g2.drawLine(x1, y1, x2, y2);
                if(i%(graphPointList.size()/10)==0) {
                    String value = Double.toString(Math.round(scores.get(graphPoints.indexOf(graphPointList)).get(i)*10000.0)/10000.0);
                    g2.drawString(value, x1 - fm.stringWidth(value), y1 - (fm.getAscent() * 2));
                }
            }
        }

        g2.setStroke(oldStroke);
        for(List<Point> graphPointList: graphPoints) {
            g2.setColor(GRAPH_POINT_COLOR[graphPoints.indexOf(graphPointList)]);
            for (int i = 0; i < graphPointList.size(); i++) {
                int x = graphPointList.get(i).x - GRAPH_POINT_WIDTH / 2;
                int y = graphPointList.get(i).y - GRAPH_POINT_WIDTH / 2;
                ;
                int ovalW = GRAPH_POINT_WIDTH;
                int ovalH = GRAPH_POINT_WIDTH;
                g2.fillOval(x, y, ovalW, ovalH);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREF_W, PREF_H);
    }

    public static void createAndShowGui() {
        List<List<Double>> scores = new ArrayList<>();
        Random random = new Random();
        int maxDataPoints = 100;
        int maxScore = 20;
        int minScore = 1;
        for(int j = 0; j<4; j++) {
            List<Double> scoresList = new ArrayList<>();
            for (int i = 0; i < maxDataPoints; i++) {
                scoresList.add((double) random.nextInt(maxScore));
            }
            scores.add(scoresList);
        }
        String graphName = "DrawGraph";
        makeGUI(scores, graphName);
    }

    public static void createAndShowGui(List<List<Double>> points, String graphName) {
        makeGUI(points, graphName);
    }

    private static void makeGUI(List<List<Double>> scores, String graphName) {
        GraphPlot mainPanel = new GraphPlot(scores);

        JFrame frame = new JFrame(graphName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocation(10,10);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
    }
}