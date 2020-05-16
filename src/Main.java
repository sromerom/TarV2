import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Benvingut a TARV2. Introduiex un fitxer Tar per començar a treballar amb ell: ");
        String pathFile = sc.nextLine();

        Tar t = new Tar(pathFile);
        boolean status = true;
        if (t.isExists()) {

            while (status) {
                System.out.println("############ MENU ############");
                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "1.-Load");

                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "2.-List");

                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "3.-Extract");

                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "4.-Help");

                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "5.-Exit");

                System.out.println("##############################");
                System.out.println();
                System.out.print("Introduiex el numero que correspongui a la comanda que vols realitzar: ");
                int comanda = sc.nextInt();

                switch (comanda) {
                    case 1:
                        System.out.println("Carregant tar a la memoria...");
                        t.expand();
                        break;
                    case 2:
                        if (t.isExpanded()) {
                            String [] fitxers = t.list();

                            for (int i = 0; i < fitxers.length; i++) {
                                System.out.printf("%10s", "");
                                System.out.printf("%d.-%s\n", (i + 1), fitxers[i]);
                            }
                        } else {
                            System.out.println("Abans de poder fer aquesta comanda, s'ha de carregar el fitxer tar a la memoria.");
                        }
                        break;
                    case 3:
                        if (t.isExpanded()) {
                            System.out.println("Extreant fitxers...");
                        } else {
                            System.out.println("Abans de poder fer aquesta comanda, s'ha de carregar el fitxer tar a la memoria.");
                        }
                        break;
                    case 4:
                        System.out.println("Mostrant ajuda...");
                        break;
                    case 5:
                        System.out.println("Sortint...");
                        status = false;
                        break;
                }
                System.out.println("----------------------------");
            }

        } else {
            System.out.println("No s'ha trobat el fitxer");
        }


    }


}
