import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

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
                System.out.printf("%-15S\n", "4.-Last Modification");

                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "5.-Checksum");

                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "6.-Size");

                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "7.-UID & GID");

                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "8.-Permissions");

                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "9.-Link File");

                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "45.-Help");

                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "46.-Exit");

                System.out.println("##############################");
                System.out.println();
                System.out.print("Introduiex el numero que correspongui a la comanda que vols realitzar: ");
                int comanda = sc.nextInt();
                Scanner sc2;
                switch (comanda) {
                    //Carregar fitxer a la memoria
                    case 1:
                        System.out.println("Carregant tar a la memoria...");
                        t.expand();
                        break;
                    //Llistam tots els fitxers
                    case 2:
                        if (t.isExpanded()) {
                            String[] fitxers = t.list();

                            for (int i = 0; i < fitxers.length; i++) {
                                System.out.printf("%10s", "");
                                System.out.printf("%d.-%s\n", (i + 1), fitxers[i]);
                            }
                        } else {
                            System.out.println("Abans de poder fer aquesta comanda, s'ha de carregar el fitxer tar a la memoria.");
                        }
                        break;
                    //Extreure tots els fitxers
                    case 3:
                        if (t.isExpanded()) {
                            sc2 = new Scanner(System.in);
                            System.out.print("Introduiex la ruta a on vols extreure els fitxers del Tar: (en blanc si vols extreure'ls en el directori a on estas): ");
                            String path2 = sc2.nextLine();
                            boolean aaa = t.extractTar(path2);
                            if (aaa) {
                                System.out.println("S'ha extret correctament");
                            } else {
                                System.out.println("No s'ha pogut extreure correctament");
                            }

                        } else {
                            System.out.println("Abans de poder fer aquesta comanda, s'ha de carregar el fitxer tar a la memoria.");
                        }
                        break;
                    case 4:
                        if (t.isExpanded()) {
                            sc2 = new Scanner(System.in);
                            System.out.print("Introduiex el nom del fitxer que vols comprovar l'ultima data de modificació: ");
                            String name = sc2.nextLine();
                            String lastModification = t.getLastModification(name);
                            System.out.println(lastModification);
                        } else {
                            System.out.println("No s'ha pogut trobar el fitxer");
                        }
                        break;
                    case 5:
                        if (t.isExpanded()) {
                            sc2 = new Scanner(System.in);
                            System.out.print("Introduiex el nom del primer fitxer: ");
                            String file1 = sc2.nextLine();
                            System.out.print("Introduiex el nom del segon fitxer: ");
                            String file2 = sc2.nextLine();


                        } else {
                            System.out.println("No s'ha pogut trobar el fitxer");
                        }
                        break;
                    case 6:
                        if (t.isExpanded()) {
                            sc2 = new Scanner(System.in);
                            System.out.print("Introduiex el nom del fitxer per mostrar el seu tamany: ");
                            String name = sc2.nextLine();

                            long size = t.getSize(name);
                            System.out.printf("El tamany del fitxer es de %d Bytes\n", size);
                        } else {
                            System.out.println("No s'ha pogut trobar el fitxer");
                        }
                        break;
                    case 7:
                        if (t.isExpanded()) {
                            sc2 = new Scanner(System.in);
                            System.out.print("Introduiex nom del fitxer per mostrar el ID del seu propietari: ");
                            String name = sc2.nextLine();

                            int[] ids = t.getIds(name);
                            System.out.println("Owner ID: " + ids[0]);
                            System.out.println("Group ID:" + ids[1]);
                        } else {
                            System.out.println("No s'ha pogut trobar el fitxer");
                        }
                        break;
                    case 8:
                        if (t.isExpanded()) {
                            sc2 = new Scanner(System.in);
                            System.out.print("Introduiex nom del fitxer per mostrar els seus permisos: ");
                            String name = sc2.nextLine();
                            int permisos = t.getPermissions(name);

                            System.out.println("Els permisos del fitxer són els següents: " + permisos);
                        } else {
                            System.out.println("No s'ha pogut trobar el fitxer");
                        }
                        break;
                    case 9:
                        if (t.isExpanded()) {
                            sc2 = new Scanner(System.in);
                            System.out.print("Introduiex el nom del fitxer per comprovar si es un fitxer amb link simbolic: ");
                            String name = sc2.nextLine();

                            String link = t.getLink(name);
                            System.out.println(link);

                        } else {
                            System.out.println("No s'ha pogut trobar el fitxer");
                        }
                        break;
                    //Un help per explicar que fa cada comanda
                    case 45:
                        System.out.println("Mostrant ajuda...");
                        break;
                    //Comanda per sortir del programa
                    case 46:
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
