/**
 * ------------------------------------------------ Jezyk Java ---------------------------------------------------------
 * --------------------------------------------------- ZAD 6 -----------------------------------------------------------
 * --------------------------------------- autor: Grzegorz Poreda, 01.12.20 --------------------------------------------
 *
 * Prosze napisac gre w kolko i krzyzyk z interfejsem graficznym dla 2 osob.
 *
 * Gra powinna obslugiwac dowolne wymiary planszy oraz dowolna ilosc jednakowych symboli w pionie, poziomie lub po
 * ukosie jako warunek wygranej. Aplikacja powinna poprosic uzytkownika o wprowadzenie tych rozmiarow przed
 * rozpoczeciem, rowniez w okienku graficznym, i na podstawie wprowadzonych wartosci przygotowac plansze do rozgrywki.
 *
 * ---------------------------------------------------------------------------------------------------------------------
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TicTacToe {

    JFrame mainFrame;

    String sign = "X";
    int size = 1;
    int toWin = 0;

    // sprawdzanie, czy ktos wygral
    public boolean checkIfWin(String[][] signs, String sign, int size) {

        int counterPoziom = 0;
        int counterPion = 0;
        int counterSkosLP = 0;
        int counterSkosPL = 0;

        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {

                // poziom
                if(signs[i][j].equals(sign)) {
                    counterPoziom++;
                }
                if(!signs[i][j].equals(sign)) {
                    counterPoziom = 0;
                }

                // pion
                if(signs[j][i].equals(sign)) {
                    counterPion++;
                }
                if(!signs[j][i].equals(sign)) {
                    counterPion = 0;
                }

                if(counterPoziom == toWin || counterPion == toWin) {
                    return true;
                }
            }

            counterPoziom = 0;
            counterPion = 0;
        }

        // skos od lewej do prawej
        if(size>1) {

            boolean diag = false;   // czy wyszlo poza diagonale
            int i = size-2;
            int subI = 0;
            int addJ = 0;
            int j = 0;
            for(int k=0; k<(1+(2*(size-2))); k++) {
                while(i<size && j<size) {

                    // skos od lewej do prawej
                    if(signs[i][j].equals(sign)) {
                        counterSkosLP++;
                    }
                    if(!signs[i][j].equals(sign)) {
                        counterSkosLP = 0;
                    }

                    // skos od prawej do lewej
                    if(signs[i][size-1-j].equals(sign)) {
                        counterSkosPL++;
                    }
                    if(!signs[i][size-1-j].equals(sign)) {
                        counterSkosPL = 0;
                    }

                    if(counterSkosLP == toWin || counterSkosPL == toWin) {
                        return true;
                    }

                    i++;
                    j++;
                }
                if(i==j)
                    diag = true;
                j=0;
                if(diag) {
                    addJ++;
                    j+=addJ;
                }
                counterSkosLP = 0;
                counterSkosPL = 0;
                subI++;

                if((size-2)-subI >= 0)
                    i=(size-2)-subI;
                if((size-2)-subI < 0)
                    i=0;
            }
        }

        return false;
    }

    // sprawdzanie, czy plansza jest pelna
    public boolean checkIfFull(String[][] signs, int size) {
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                if(signs[i][j].equals(""))
                    return false;
            }
        }
        return true;
    }

    private void createAndShowGUI() {
        // nowe okno o tytule TicTacToe
        this.mainFrame = new JFrame("Tic Tac Toe");
        // zamkniecie okna spowoduje zakonczenie programu
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("Opcje");

        JPanel gamePanel = new JPanel();;

        JPanel preGamePanel = new JPanel();
        preGamePanel.setLayout(new GridLayout(3,2));

        JMenuItem item1 = new JMenuItem("Nowa gra");
        item1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.getContentPane().remove(gamePanel);
                gamePanel.removeAll();
                mainFrame.getContentPane().remove(preGamePanel);
                preGamePanel.removeAll();

                preGamePanel.add(new JLabel("Rozmiar planszy:"));
                JTextField textField1 = new JTextField("");
                preGamePanel.add(textField1);
                preGamePanel.add(new JLabel("Liczba pol do wygranej:"));
                JTextField textField2 = new JTextField("");
                preGamePanel.add(textField2);

                JButton start = new JButton("Start");
                start.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if(!textField1.getText().equals("") && !textField2.getText().equals("")
                                && Integer.parseInt(textField1.getText()) >= Integer.parseInt(textField2.getText())) {

                            size = Integer.parseInt(textField1.getText());
                            toWin = Integer.parseInt(textField2.getText());

                            gamePanel.setLayout(new GridLayout(size,size));

                            mainFrame.getContentPane().remove(gamePanel);
                            gamePanel.removeAll();
                            mainFrame.getContentPane().remove(preGamePanel);
                            preGamePanel.removeAll();

                            String[][] signs = new String[size][size];
                            for(int i=0; i<size; i++) {
                                for (int j = 0; j < size; j++) {
                                    signs[i][j] = "";
                                }
                            }
                            for(int i=0; i<size; i++) {
                                for(int j=0; j<size; j++) {
                                    JButton b = new JButton("");
                                    final int currI = i;
                                    final int currJ = j;
                                    b.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            if (b.getText().equals("")) {
                                                // zmiana tekstu na przycisku
                                                b.setText(sign);

                                                signs[currI][currJ] = sign;
                                                System.out.println("Przycisk " + currI + ":" + currJ + " = " +
                                                        signs[currI][currJ]);
                                                if(checkIfWin(signs, sign, size)) {
                                                    JOptionPane.showMessageDialog(mainFrame,"Wygrywa " + sign + "!");
                                                    mainFrame.getContentPane().remove(gamePanel);
                                                    mainFrame.revalidate();
                                                    mainFrame.pack();
                                                }
                                                if(checkIfFull(signs, size)) {
                                                    JOptionPane.showMessageDialog(mainFrame,"Remis!");
                                                    mainFrame.getContentPane().remove(gamePanel);
                                                    mainFrame.revalidate();
                                                    mainFrame.pack();
                                                }

                                                // zamiana znaku
                                                if (b.getText().equals("X"))
                                                    sign = "O";
                                                else
                                                    sign = "X";
                                            }
                                            else
                                                JOptionPane.showMessageDialog(mainFrame, "Pole zajete!");
                                        }
                                    });
                                    gamePanel.add(b);
                                }
                            }
                            mainFrame.getContentPane().add(gamePanel);
                            mainFrame.revalidate();     // uaktualnij okno
                            mainFrame.pack();
                        }
                        else if(!textField1.getText().equals("") && !textField2.getText().equals("")
                                && Integer.parseInt(textField1.getText()) < Integer.parseInt(textField2.getText())) {
                            JOptionPane.showMessageDialog(mainFrame,"Liczba pol do wygranej nie moze byc " +
                                    "wieksza niz rozmiar planszy!");
                        }
                        else {
                            JOptionPane.showMessageDialog(mainFrame,"Nie podano wszystkich argumentow!");
                        }
                    }
                });
                preGamePanel.add(start);
                mainFrame.getContentPane().add(preGamePanel);
                mainFrame.revalidate();
                mainFrame.pack();
            }
        });
        menu1.add(item1);

        JMenuItem item2 = new JMenuItem("Zasady gry");
        item2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainFrame,"2 graczy na przemian zaznacza wybrane pole swoim " +
                        "symbolem.\nWygrywa ten z graczy, ktoremu sie uda zaznaczyc okreslona ilosc\npol pod rzad: " +
                        "pionowo, poziomo albo po skosie.");
            }
        });
        menu1.add(item2);

        JMenuItem item3 = new JMenuItem("Wyjscie");
        item3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
            }
        });
        menu1.add(item3);

        menuBar.add(menu1);
        mainFrame.setJMenuBar(menuBar);

        // dopasowanie rozmiarow okna do umieszczonych w nim komponentow
        mainFrame.pack();
        // okno zostanie umieszczone na srodku ekranu
        mainFrame.setLocationRelativeTo(null);
        // wyswietlenie okna
        mainFrame.setVisible(true);
    }
    public static void main(String[] args) {

        TicTacToe gra = new TicTacToe();

        // stworzenie nowego watku, w ktorym zostanie 'uruchomione' okno
        SwingUtilities.invokeLater(gra::createAndShowGUI);
    }
}

