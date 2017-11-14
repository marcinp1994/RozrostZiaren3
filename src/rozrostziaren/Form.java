package rozrostziaren;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Form extends javax.swing.JFrame {

    int wid = 300, hei = 300;
    int[][] currentMove;
    ArrayList<Integer> array = new ArrayList<Integer>();
    Set<Integer> setOfId = new LinkedHashSet<>();
    int[][] nextMove;
    List<Color> listColor = new ArrayList<Color>();
    Color inclusionColor;
    Color structuresColor;
    int xSize, ySize = 0;
    int xGB = 0;
    int yGB = 0;

    boolean isSize = false;
    boolean play;
    Image offScrImg;
    Graphics offScrGraph;
    Random random = new Random();
    Punkt punkt;
    static int ID = 0;
    static String sasiedztwo = "von Nuemann";
    boolean isPeriod = true;
    final long A;
    final int B;
    static long ro;
    int R = 0;
    boolean isCircle;
    int amountSquere = 0;
    int amountCircle = 0;

    public Form() {
        this.A = 86711000000000L;
        this.B = 941;
        initComponents();
        setTitle("Rozrost Ziaren - Marcin");
        jPanel1.setVisible(false);
    }

    private int checkNeighborNeumann(int i, int j) {
        int seed = -1;

        int[] xtemp = new int[2];
        int[] ytemp = new int[2];

        if (isPeriod) {
            xtemp[0] = (i == 0 ? wid - 1 : i - 1);
            xtemp[1] = (i == wid - 1 ? 0 : i + 1);

            ytemp[0] = (j == 0 ? hei - 1 : j - 1);
            ytemp[1] = (j == hei - 1 ? 0 : j + 1);
        } else {
            xtemp[0] = (i == 0 ? -1 : i - 1);
            xtemp[1] = (i == wid - 1 ? -1 : i + 1);

            ytemp[0] = (j == 0 ? -1 : j - 1);
            ytemp[1] = (j == hei - 1 ? -1 : j + 1);
        }

        int[] neighboursId = new int[ID + 1];
        for (int k = 0; k <= ID; k++) {
            neighboursId[k] = 0;
        }

        for (int k = 0; k < 2; k++) {
            if (xtemp[k] == -2 || xtemp[k] == -3) {
                continue;
            } else {
                if (xtemp[k] != -1) {
                    int seedId = currentMove[xtemp[k]][j];
                    if (seedId != -1 && seedId != -2 && seedId != -3 && !setOfId.contains(seedId)) {
                        neighboursId[seedId]++;
                    }
                }
            }
            if (ytemp[k] == -2 || ytemp[k] == -3) {
                continue;
            } else {
                if (ytemp[k] != -1) {
                    int seedId = currentMove[i][ytemp[k]];
                    if (seedId != -1 && seedId != -2 && seedId != -3 && !setOfId.contains(seedId)) {
                        neighboursId[seedId]++;
                    }
                }
            }
        }

        int max = -1;
        for (int k = 0; k <= ID; k++) {
            if (neighboursId[k] > max && neighboursId[k] > 0) {
                seed = k;
            } else if (neighboursId[k] == max) {
                seed = random.nextInt(2) == 0 ? k : seed;
            }
        }

        return seed;
    }

    private int checkNeighborMoore(int i, int j) {
        int seed = -1;

        int[] xtemp = new int[3];
        int[] ytemp = new int[3];

        if (isPeriod) {
            xtemp[0] = (i == 0 ? wid - 1 : i - 1);
            xtemp[1] = i;
            xtemp[2] = (i == wid - 1 ? 0 : i + 1);

            ytemp[0] = (j == 0 ? hei - 1 : j - 1);
            ytemp[1] = j;
            ytemp[2] = (j == hei - 1 ? 0 : j + 1);
        } else {
            xtemp[0] = (i == 0 ? -1 : i - 1);
            xtemp[1] = i;
            xtemp[2] = (i == wid - 1 ? -1 : i + 1);

            ytemp[0] = (j == 0 ? -1 : j - 1);
            ytemp[1] = j;
            ytemp[2] = (j == hei - 1 ? -1 : j + 1);
        }

        int[] neighboursId = new int[ID + 1];
        for (int k = 0; k <= ID; k++) {
            neighboursId[k] = 0;
        }

        for (int k = 0; k < 3; k++) {
            {
                for (int l = 0; l < 3; l++) {
                    if (xtemp[k] == -2 && ytemp[l] == -2) {
                        continue;
                    } else {
                        if (xtemp[k] != -1 && ytemp[l] != -1) {
                            int seedId = currentMove[xtemp[k]][ytemp[l]];
                            if (seedId != -1 && seedId != -2) {
                                neighboursId[seedId]++;
                            }
                        }
                    }
                }
            }
        }

        int max = -1;
        for (int k = 0; k <= ID; k++) {
            if (neighboursId[k] > max && neighboursId[k] > 0) {
                seed = k;
            } else if (neighboursId[k] == max) {
                seed = random.nextInt(2) == 0 ? k : seed;
            }
        }
        return seed;
    }

    private int checkNeighborMooreWithRule1(int i, int j) {
        int seed = -1;

        int[] xtemp = new int[3];
        int[] ytemp = new int[3];

        if (isPeriod) {
            xtemp[0] = (i == 0 ? wid - 1 : i - 1);
            xtemp[1] = i;
            xtemp[2] = (i == wid - 1 ? 0 : i + 1);

            ytemp[0] = (j == 0 ? hei - 1 : j - 1);
            ytemp[1] = j;
            ytemp[2] = (j == hei - 1 ? 0 : j + 1);
        } else {
            xtemp[0] = (i == 0 ? -1 : i - 1);
            xtemp[1] = i;
            xtemp[2] = (i == wid - 1 ? -1 : i + 1);

            ytemp[0] = (j == 0 ? -1 : j - 1);
            ytemp[1] = j;
            ytemp[2] = (j == hei - 1 ? -1 : j + 1);
        }

        int[] neighboursId = new int[ID + 1];
        for (int k = 0; k <= ID; k++) {
            neighboursId[k] = 0;
        }

        for (int k = 0; k < 3; k++) {
            {
                for (int l = 0; l < 3; l++) {
                    if (xtemp[k] == -2 && ytemp[l] == -2) {
                        continue;
                    } else {
                        if (xtemp[k] != -1 && ytemp[l] != -1) {
                            int seedId = currentMove[xtemp[k]][ytemp[l]];
                            if (seedId != -1 && seedId != -2) {
                                neighboursId[seedId]++;
                            }
                        }
                    }
                }
            }
        }

        int max = -1;
        for (int k = 0; k <= ID; k++) {
            if (neighboursId[k] >= 5 && neighboursId[k] <= 8) {
                seed = k;
            }
        }
        return seed;
    }

    private int checkNeighborMooreWithRule2(int i, int j) {
        int seed = -1;

        int[] xtemp = new int[2];
        int[] ytemp = new int[2];

        if (isPeriod) {
            xtemp[0] = (i == 0 ? wid - 1 : i - 1);
            xtemp[1] = (i == wid - 1 ? 0 : i + 1);

            ytemp[0] = (j == 0 ? hei - 1 : j - 1);
            ytemp[1] = (j == hei - 1 ? 0 : j + 1);
        } else {
            xtemp[0] = (i == 0 ? -1 : i - 1);
            xtemp[1] = (i == wid - 1 ? -1 : i + 1);

            ytemp[0] = (j == 0 ? -1 : j - 1);
            ytemp[1] = (j == hei - 1 ? -1 : j + 1);
        }

        int[] neighboursId = new int[ID + 1];
        for (int k = 0; k <= ID; k++) {
            neighboursId[k] = 0;
        }

        for (int k = 0; k < 2; k++) {
            if (xtemp[k] == -2) {
                continue;
            } else {
                if (xtemp[k] != -1) {
                    int seedId = currentMove[xtemp[k]][j];
                    if (seedId != -1 && seedId != -2) {
                        neighboursId[seedId]++;
                    }
                }
            }
            if (ytemp[k] == -2) {
                continue;
            } else {
                if (ytemp[k] != -1) {
                    int seedId = currentMove[i][ytemp[k]];
                    if (seedId != -1 && seedId != -2) {
                        neighboursId[seedId]++;
                    }
                }
            }
        }

        int max = -1;
        for (int k = 0; k <= ID; k++) {
            if (neighboursId[k] >= 3 && neighboursId[k] <= 4) {
                seed = k;
            }
        }

        return seed;
    }

    private int checkNeighborMooreWithRule3(int i, int j) {
        int seed = -1;

        int[] xtemp = new int[2];
        int[] ytemp = new int[2];

        if (isPeriod) {
            xtemp[0] = (i == 0 ? wid - 1 : i - 1);
            xtemp[1] = (i == wid - 1 ? 0 : i + 1);

            ytemp[0] = (j == 0 ? hei - 1 : j - 1);
            ytemp[1] = (j == hei - 1 ? 0 : j + 1);
        } else {
            xtemp[0] = (i == 0 ? -1 : i - 1);
            xtemp[1] = (i == wid - 1 ? -1 : i + 1);

            ytemp[0] = (j == 0 ? -1 : j - 1);
            ytemp[1] = (j == hei - 1 ? -1 : j + 1);
        }

        int[] neighboursId = new int[ID + 1];
        for (int k = 0; k <= ID; k++) {
            neighboursId[k] = 0;
        }

        if (xtemp[0] != -1 && ytemp[0] != -1) {
            int seedId = currentMove[xtemp[0]][ytemp[0]];
            if (seedId != -1 && seedId != -2) {
                neighboursId[seedId]++;
            }
        }
        if (xtemp[0] != -1 && ytemp[1] != -1) {
            int seedId = currentMove[xtemp[0]][ytemp[1]];
            if (seedId != -1 && seedId != -2) {
                neighboursId[seedId]++;
            }
        }
        if (xtemp[1] != -1 && ytemp[0] != -1) {
            int seedId = currentMove[xtemp[1]][ytemp[0]];
            if (seedId != -1 && seedId != -2) {
                neighboursId[seedId]++;
            }
        }
        if (xtemp[1] != -1 && ytemp[1] != -1) {
            int seedId = currentMove[xtemp[1]][ytemp[1]];
            if (seedId != -1 && seedId != -2) {
                neighboursId[seedId]++;
            }
        }

        int max = -1;
        for (int k = 0; k <= ID; k++) {
            if (neighboursId[k] >= 3 && neighboursId[k] <= 4) {
                seed = k;
            }
        }

        return seed;
    }

    private int checkNeighborMooreWithRule4(int i, int j) {
        int seed = -1;

        int[] xtemp = new int[3];
        int[] ytemp = new int[3];

        if (isPeriod) {
            xtemp[0] = (i == 0 ? wid - 1 : i - 1);
            xtemp[1] = i;
            xtemp[2] = (i == wid - 1 ? 0 : i + 1);

            ytemp[0] = (j == 0 ? hei - 1 : j - 1);
            ytemp[1] = j;
            ytemp[2] = (j == hei - 1 ? 0 : j + 1);
        } else {
            xtemp[0] = (i == 0 ? -1 : i - 1);
            xtemp[1] = i;
            xtemp[2] = (i == wid - 1 ? -1 : i + 1);

            ytemp[0] = (j == 0 ? -1 : j - 1);
            ytemp[1] = j;
            ytemp[2] = (j == hei - 1 ? -1 : j + 1);
        }

        int[] neighboursId = new int[ID + 1];
        for (int k = 0; k <= ID; k++) {
            neighboursId[k] = 0;
        }

        for (int k = 0; k < 3; k++) {
            {
                for (int l = 0; l < 3; l++) {
                    if (xtemp[k] == -2 && ytemp[l] == -2) {
                        continue;
                    } else {
                        if (xtemp[k] != -1 && ytemp[l] != -1) {
                            int seedId = currentMove[xtemp[k]][ytemp[l]];
                            if (seedId != -1 && seedId != -2) {
                                neighboursId[seedId]++;
                            }
                        }
                    }
                }
            }
        }

        int probability = Integer.parseInt(jTextField7.getText());

        OptionalInt randInt = random.ints(0, 100).findFirst();

        int max = -1;
        if (randInt.getAsInt() <= probability) {
            for (int k = 0; k <= ID; k++) {
                if (neighboursId[k] > max && neighboursId[k] > 0) {
                    seed = k;
                } else if (neighboursId[k] == max) {
                    seed = random.nextInt(2) == 0 ? k : seed;
                }
            }
        }
        return seed;
    }

    private void repain() {
        offScrGraph.setColor(jPanel1.getBackground());
        offScrGraph.fillRect(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
        for (int i = 0; i < wid; i++) {
            for (int j = 0; j < hei; j++) {
                if (currentMove[i][j] != -1) {
                    if (currentMove[i][j] == -2) {
                        offScrGraph.setColor(inclusionColor);
                        offScrGraph.fillRect(i, j, jPanel1.getWidth() / wid, jPanel1.getHeight() / hei);
                    } else if (currentMove[i][j] == -3) {
                        offScrGraph.setColor(structuresColor);
                        offScrGraph.fillRect(i, j, jPanel1.getWidth() / wid, jPanel1.getHeight() / hei);
                    } else {
                        offScrGraph.setColor(listColor.get(currentMove[i][j]));
                        offScrGraph.fillRect(i, j, jPanel1.getWidth() / wid, jPanel1.getHeight() / hei);
                    }
                    // int x = j; //* jPanel1.getWidth() / wid;
                    //int y = i; //* jPanel1.getHeight() / hei;
                    //offScrGraph.fillRect(x, y, jPanel1.getHeight() / hei + 1, jPanel1.getWidth() / wid);
                }
            }
        }

        jPanel1.getGraphics().drawImage(offScrImg, 0, 0, jPanel1);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem3 = new javax.swing.JMenuItem();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        popupMenu1 = new java.awt.PopupMenu();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton4 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        label1 = new java.awt.Label();
        label2 = new java.awt.Label();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        jMenuItem3.setText("jMenuItem3");

        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("jRadioButtonMenuItem1");

        popupMenu1.setLabel("popupMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setDoubleBuffered(false);
        jPanel1.setFocusable(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 300));
        jPanel1.setRequestFocusEnabled(false);
        jPanel1.setVerifyInputWhenFocusTarget(false);
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
        });
        jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanel1ComponentResized(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Random");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "von Nuemann", "Moore" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("non-periodical");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton4.setText("Clear");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel1.setText("xSize");

        jLabel2.setText("ySize");

        jButton3.setText("Load Size");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setText("Square");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel3.setText("Behind simulation");

        jButton6.setText("Circle");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel4.setText("After simulation");

        jButton8.setText("Circle");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton7.setText("Square");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jTextField3.setText("0");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jTextField4.setText("0");
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jTextField5.setText("0");

        jTextField6.setText("0");

        jTextField7.setText("0");

        label1.setText("Probability:");

        label2.setText("%");

        jButton9.setText("1 color structures");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("Rand");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setText("4 color structures");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setText("GB");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText("Clear space");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setText("1 GB");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jMenu1.setText("File");

        jMenuItem1.setText("Export");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem4.setText("Import from TXT");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem2.setText("Import From BMP");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBox1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jButton12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton13)
                                .addGap(26, 26, 26)
                                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(36, 36, 36)
                                .addComponent(jLabel2)
                                .addGap(12, 12, 12)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(47, 47, 47)
                                        .addComponent(jButton4))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(27, 27, 27)
                                        .addComponent(jButton3)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(22, 22, 22)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(11, 11, 11)
                                        .addComponent(jButton7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(8, 8, 8)))))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(38, 38, 38))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(46, 46, 46))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(24, 24, 24)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton6)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton11)
                                    .addComponent(jButton9))
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton5)
                                    .addComponent(jButton6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton7)
                                    .addComponent(jButton8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2)
                                        .addComponent(jButton4))
                                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton12)
                                    .addComponent(jButton13)
                                    .addComponent(jButton14))
                                .addGap(3, 3, 3)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        jPanel1.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (isSize == false) {
            JOptionPane.showMessageDialog(null, "You have to enater a SIZE!");
        } else {
            play = !play;
            if (play) {
                jButton1.setText("Pause");
            } else {
                jButton1.setText("Start");
            }

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        Color col = new Color(random.nextInt(256), random.nextInt(240), random.nextInt(256));
        listColor.add(col);
        int x = evt.getX();
        int y = evt.getY();
        xGB = x;
        yGB = y;
        currentMove[x][y] = ID;
        ID++;

        repain();

    }//GEN-LAST:event_jPanel1MouseClicked

    private void jPanel1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel1ComponentResized

    }//GEN-LAST:event_jPanel1ComponentResized

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (isSize == false) {
            JOptionPane.showMessageDialog(null, "You have to enater a SIZE!");
        } else {
            for (int k = 0; k < 20; k++) {
                Color col = new Color(random.nextInt(256), random.nextInt(240), random.nextInt(256));
                listColor.add(col);
                OptionalInt randomNumber = random.ints(0, wid).findFirst();
                OptionalInt randomNumber2 = random.ints(0, hei).findFirst();
                int x = randomNumber.getAsInt();
                int y = randomNumber2.getAsInt();

                currentMove[x][y] = ID;
                ID++;
            }
            repain();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

        sasiedztwo = jComboBox1.getSelectedItem().toString();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (jCheckBox1.isSelected()) {
            isPeriod = false;
        } else {
            isPeriod = true;
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        for (int i = 0; i < wid; i++) {
            for (int j = 0; j < hei; j++) {
                currentMove[i][j] = -1;
            }
        }
        listColor.clear();
        ID = 0;
        repain();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        isSize = true;
        xSize = Integer.parseInt(jTextField1.getText());
        ySize = Integer.parseInt(jTextField2.getText());
        wid = xSize;
        hei = ySize;
        currentMove = new int[wid][hei];
        nextMove = new int[wid][hei];
        jPanel1.setPreferredSize(new Dimension(wid, hei));
        jPanel1.setVisible(true);
        pack();

        for (int i = 0; i < wid; i++) {
            for (int j = 0; j < hei; j++) {
                currentMove[i][j] = -1;
            }
        }
        offScrImg = createImage(jPanel1.getWidth(), jPanel1.getHeight());
        offScrGraph = offScrImg.getGraphics();
        Timer time = new Timer();
        TimerTask task = new TimerTask() {

            public void run() {
                if (play) {
                    nextMove = new int[wid][hei];
                    for (int i = 0; i < wid; i++) {
                        for (int j = 0; j < hei; j++) {
                            if (currentMove[i][j] == -1) {

                                if (sasiedztwo.equals("Moore")) {
                                    if (currentMove[i][j] == -2) {

                                    } else {
                                        nextMove[i][j] = checkNeighborMooreWithRule1(i, j);
                                        if (nextMove[i][j] == -1) {
                                            nextMove[i][j] = checkNeighborMooreWithRule2(i, j);
                                            if (nextMove[i][j] == -1) {
                                                nextMove[i][j] = checkNeighborMooreWithRule3(i, j);
                                                if (nextMove[i][j] == -1) {
                                                    nextMove[i][j] = checkNeighborMooreWithRule4(i, j);
                                                }
                                            }
                                        }
                                        if (currentMove[i][j] != nextMove[i][j]) {
                                            array.add(i);
                                            array.add(j);
                                        }
                                    }
                                }
                                if (sasiedztwo.equals("von Nuemann")) {
                                    if (currentMove[i][j] == -2 || currentMove[i][j] == -3 || setOfId.contains(currentMove[i][j])) {

                                    } else {
                                        nextMove[i][j] = checkNeighborNeumann(i, j);
                                        if (currentMove[i][j] != nextMove[i][j]) {
                                            array.add(i);
                                            array.add(j);
                                        }
                                    }
                                }
                            } else {
                                nextMove[i][j] = currentMove[i][j];
                            }
                        }
                    }
                    currentMove = nextMove;

                    repain();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        time.scheduleAtFixedRate(task, 0, 30);

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

        BufferedImage bImg = new BufferedImage(jPanel1.getWidth(), jPanel1.getHeight(), BufferedImage.TYPE_INT_RGB);
        bImg.createGraphics().drawImage(offScrImg, 0, 0, this /* observer */);
        jPanel1.paintAll(offScrGraph);
        try {
            JFileChooser jfc = new JFileChooser();
            int retVal = jfc.showSaveDialog(null);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                ImageIO.write(bImg, "bmp", jfc.getSelectedFile());
            }
//            if (ImageIO.write(bImg, "bmp", new File("./output_image.bmp"))) {
//                System.out.println("-- saved");
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            File fout = new File("out.txt");
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            bw.write(wid + "," + hei);
            bw.newLine();
            for (int i = 0; i < wid; i++) {
                for (int j = 0; j < hei; j++) {
                    bw.write(i + "," + j + "," + currentMove[i][j]);
                    bw.newLine();
                }
            }
            bw.close();

        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            jPanel1.setVisible(true);
            JFileChooser fc = new JFileChooser();
            int result = fc.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String sname = file.getAbsolutePath();
                offScrImg = ImageIO.read(new File(sname));

                //offScrImg = ImageIO.read(new File("./output_image.bmp"));
                jPanel1.setPreferredSize(new Dimension(offScrImg.getWidth(null), offScrImg.getHeight(null)));
                pack();
                offScrGraph = offScrImg.getGraphics();
            }
            Timer time = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    jPanel1.getGraphics().drawImage(offScrImg, 0, 0, null);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            time.scheduleAtFixedRate(task, 0, 30);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed

        File f = new File("./out.txt");

        BufferedReader b = null;
        try {
            b = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
        }

        String readLine = null;
        try {
            readLine = b.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
        }
        wid = Integer.parseInt(readLine.split(",")[0]);
        hei = Integer.parseInt(readLine.split(",")[1]);
        currentMove = new int[hei][wid];
        jPanel1.setPreferredSize(new Dimension(wid, hei));
        jPanel1.setVisible(true);
        offScrImg = createImage(wid, hei);
        offScrGraph = offScrImg.getGraphics();
        pack();
        int[][] fromfile = new int[wid][hei];
        try {
            while ((readLine = b.readLine()) != null) {
                currentMove[Integer.parseInt(readLine.split(",")[0])][Integer.parseInt(readLine.split(",")[1])] = Integer.parseInt(readLine.split(",")[2]);
            }
        } catch (IOException ex) {
            Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int k = 0; k < 20; k++) {
            Color col = new Color(random.nextInt(256), random.nextInt(240), random.nextInt(256));
            listColor.add(col);
        }
        Timer time = new Timer();
        TimerTask task = new TimerTask() {

            public void run() {
                repain();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        time.scheduleAtFixedRate(task, 0, 30);

    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Random rn = new Random();
        amountSquere = Integer.parseInt(jTextField3.getText());

        for (int i = 0; i < amountSquere; i++) {
            inclusionColor = new Color(0, 0, 0);
            OptionalInt randomNumber = random.ints(12, wid - 12).findFirst();
            OptionalInt randomNumber2 = random.ints(12, hei - 12).findFirst();
            int x = randomNumber.getAsInt();
            int y = randomNumber2.getAsInt();
            for (int k = x - 4; k <= x + 4; k++) {
                for (int j = y - 4; j <= y + 4; j++) {
                    currentMove[k][j] = -2;
                }
            }
            repain();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        amountCircle = Integer.parseInt(jTextField4.getText());
        for (int i = 0; i < amountCircle; i++) {
            inclusionColor = new Color(0, 0, 0);
            Random rand = new Random();
            OptionalInt randomNumber = random.ints(26, wid - 26).findFirst();
            OptionalInt randomNumber2 = random.ints(26, hei - 26).findFirst();
            int x = randomNumber.getAsInt();
            int y = randomNumber2.getAsInt();
            currentMove[x][y] = -2;
            for (int k = x - 5; k <= x + 5; k++) {
                for (int j = y - 5; j <= y + 5; j++) {
                    if ((k - x) * (k - x) + (j - y) * (j - y) <= 5 * 5) {
                        currentMove[k][j] = -2;
                    }
                }
            }
            repain();
        }


    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        int amountCir = Integer.parseInt(jTextField6.getText());
        for (int i = 0; i < amountCir; i++) {
            inclusionColor = new Color(0, 0, 0);
            Random rand = new Random();
            OptionalInt randomNumber = random.ints(26, wid - 26).limit(1).findFirst();
            OptionalInt randomNumber2 = random.ints(26, hei - 26).limit(1).findFirst();
            int x = randomNumber.getAsInt();
            int y = randomNumber2.getAsInt();

            for (int xi = x; xi < wid; xi++) {
                for (int yi = y; yi < hei; yi++) {
                    int a = currentMove[xi][yi];
                    int b = checkNeighborNeumann(xi, yi);
                    if (a == b) {
                        continue;
                    } else {
                        if (b != -2) {
                            for (int k = xi - 5; k <= xi + 5; k++) {
                                for (int j = yi - 5; j <= yi + 5; j++) {
                                    if ((k - xi) * (k - xi) + (j - yi) * (j - yi) <= 5 * 5) {
                                        try {
                                            currentMove[k][j] = -2;
                                        } catch (Exception e) {
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }

                }
                break;
            }
            repain();
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        int amountSq = Integer.parseInt(jTextField5.getText());
        for (int i = 0; i < amountSq; i++) {
            inclusionColor = new Color(0, 0, 0);
            Random rand = new Random();
            OptionalInt randomNumber = random.ints(12, wid - 12).findFirst();
            OptionalInt randomNumber2 = random.ints(12, hei - 12).findFirst();
            int x = randomNumber.getAsInt();
            int y = randomNumber2.getAsInt();
            for (int xi = x; xi < wid; xi++) {
                for (int yi = y; yi < hei; yi++) {
                    int a = currentMove[xi][yi];
                    int b = checkNeighborNeumann(xi, yi);
                    if (a == b) {
                        continue;
                    } else {
                        if (b != -2) {
                            for (int k = xi - 3; k <= xi + 3; k++) {
                                for (int j = yi - 3; j <= yi + 3; j++) {
                                    try {
                                        currentMove[k][j] = -2;
                                    } catch (Exception e) {

                                    }
                                }
                            }
                            break;
                        }
                    }

                }
                break;
            }
            repain();
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        amountSquere = Integer.parseInt(jTextField3.getText());
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        amountCircle = Integer.parseInt(jTextField4.getText());
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        Set<Integer> setId = new LinkedHashSet<>();
        structuresColor = new Color(random.nextInt(256), random.nextInt(240), random.nextInt(256));
        while (setId.size() != 4) {
            setId.add(random.ints(0, ID).findAny().getAsInt());
        }
        for (int i = 0; i < wid; i++) {
            for (int j = 0; j < hei; j++) {
                if (setId.contains(currentMove[i][j])) {
                    currentMove[i][j] = -3;
                } else {
                    currentMove[i][j] = -1;
                }
            }
        }
        repain();

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        listColor.clear();;
        ID = 0;
        for (int k = 0; k < 30; k++) {
            Color col = new Color(random.nextInt(256), random.nextInt(240), random.nextInt(256));
            listColor.add(col);

            int x, y;

            do {
                OptionalInt randomNumber = random.ints(0, wid).findFirst();
                OptionalInt randomNumber2 = random.ints(0, hei).findFirst();
                x = randomNumber.getAsInt();
                y = randomNumber2.getAsInt();
            } while (currentMove[x][y] != -1);
            currentMove[x][y] = ID;
            ID++;
        }
        repain();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        structuresColor = new Color(random.nextInt(256), random.nextInt(240), random.nextInt(256));
        while (setOfId.size() != 4) {
            setOfId.add(random.ints(0, ID).findAny().getAsInt());
        }
        for (int i = 0; i < wid; i++) {
            for (int j = 0; j < hei; j++) {
                if (setOfId.contains(currentMove[i][j])) {
                } else {
                    currentMove[i][j] = -1;
                }
            }
        }
        repain();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        inclusionColor = new Color(0, 0, 0);
        for (int i = 0; i < wid; i++) {
            for (int j = 0; j < hei; j++) {
                int a = currentMove[i][j];
                int b = checkNeighborNeumann(i, j);
                if (a == b) {
                    continue;
                } else {
                    // if (b != -2) {
                    for (int k = i; k <= i; k++) {
                        for (int l = j; l <= j; l++) {
                            try {
                                currentMove[k][l] = -2;
                            } catch (Exception e) {

                            }
                        }
                    }
                    //}
                }

            }
        }
        repain();

    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        for (int i = 0; i < wid; i++) {
            for (int j = 0; j < hei; j++) {
                if (currentMove[i][j] != -2) {
                    currentMove[i][j] = -1;
                }
            }
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        inclusionColor = new Color(0, 0, 0);
        int id = random.ints(0, ID).findAny().getAsInt();
        for (int i = 0; i < wid; i++) {
            for (int j = 0; j < hei; j++) {
                int a = currentMove[i][j];
                int b = checkNeighborMoore(i, j);
                if (a == b) {
                    continue;
                } else {
                     if (b == id || a == id) {
                    for (int k = i; k <= i; k++) {
                        for (int l = j; l <= j; l++) {
                            try {
                                currentMove[k][l] = -2;
                            } catch (Exception e) {

                            }
                        }
                    }
                    }
                }

            }
        }
        repain();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
         inclusionColor = new Color(0, 0, 0);
        int x = evt.getX();
        int y = evt.getY();
        int id = currentMove[x][y];
        for (int i = 0; i < wid; i++) {
            for (int j = 0; j < hei; j++) {
                int a = currentMove[i][j];
                int b = checkNeighborMoore(i, j);
                if (a == b) {
                    continue;
                } else {
                     if (b == id || a == id) {
                    for (int k = i; k <= i; k++) {
                        for (int l = j; l <= j; l++) {
                            try {
                                currentMove[k][l] = -2;
                            } catch (Exception e) {

                            }
                        }
                    }
                    }
                }

            }
        }
        repain();
    }//GEN-LAST:event_jPanel1MousePressed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Form().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private java.awt.Label label1;
    private java.awt.Label label2;
    private java.awt.PopupMenu popupMenu1;
    // End of variables declaration//GEN-END:variables
}
