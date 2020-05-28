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
                System.out.printf("%-15S\n", "4.-Last Modification");

                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "5.-Size");

                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "6.-UID & GID");

                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "7.-Permissions");

                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "8.-Link File");

                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "9.-Help");

                System.out.printf("%11s", "");
                System.out.printf("%-15S\n", "10.-Exit");

                System.out.println("##############################");
                System.out.println();
                System.out.print("Introduiex el numero que correspongui a la comanda que vols realitzar: ");
                int comanda = sc.nextInt();
                Scanner sc2;
                switch (comanda) {
                    //Carregar fitxer a la memoria
                    case 1:
                        generaEspais();

                        t.expand();
                        System.out.println("Carregat tar a la memoria...");
                        break;
                    //Llistam tots els fitxers
                    case 2:
                        if (t.isExpanded()) {
                            generaEspais();

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
                            generaEspais();

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
                        //Aconseguir ultima data de modificacio
                    case 4:
                        if (t.isExpanded()) {
                            sc2 = new Scanner(System.in);
                            System.out.print("Introduiex el nom del fitxer que vols comprovar l'ultima data de modificació: ");
                            String name = sc2.nextLine();
                            generaEspais();

                            String lastModification = t.getLastModification(name);
                            System.out.println(lastModification);
                        } else {
                            System.out.println("Abans de poder fer aquesta comanda, s'ha de carregar el fitxer tar a la memoria.");
                        }
                        break;
                        //Obtenir tamany d'un fitxer en concret
                    case 5:
                        if (t.isExpanded()) {
                            sc2 = new Scanner(System.in);
                            System.out.print("Introduiex el nom del fitxer per mostrar el seu tamany: ");
                            String name = sc2.nextLine();
                            generaEspais();

                            long size = t.getSize(name);
                            System.out.printf("El tamany del fitxer es de %d Bytes\n", size);
                        } else {
                            System.out.println("Abans de poder fer aquesta comanda, s'ha de carregar el fitxer tar a la memoria.");
                        }
                        break;
                        //Obtenir owner i group ID
                    case 6:
                        if (t.isExpanded()) {
                            sc2 = new Scanner(System.in);
                            System.out.print("Introduiex nom del fitxer per mostrar el ID del seu propietari: ");
                            String name = sc2.nextLine();
                            generaEspais();

                            int[] ids = t.getIds(name);
                            System.out.println("Owner ID: " + ids[0]);
                            System.out.println("Group ID:" + ids[1]);
                        } else {
                            System.out.println("Abans de poder fer aquesta comanda, s'ha de carregar el fitxer tar a la memoria.");
                        }
                        break;
                        //Obtenir permisos d'un fitxer en concret
                    case 7:
                        if (t.isExpanded()) {
                            sc2 = new Scanner(System.in);
                            System.out.print("Introduiex nom del fitxer per mostrar els seus permisos: ");
                            String name = sc2.nextLine();
                            generaEspais();

                            int permisos = t.getPermissions(name);

                            System.out.println("Els permisos del fitxer són els següents: " + permisos);
                        } else {
                            System.out.println("Abans de poder fer aquesta comanda, s'ha de carregar el fitxer tar a la memoria.");
                        }
                        break;
                        //Comprovacio link
                    case 8:
                        if (t.isExpanded()) {
                            sc2 = new Scanner(System.in);
                            System.out.print("Introduiex el nom del fitxer per comprovar si es un fitxer amb link simbolic: ");
                            String name = sc2.nextLine();
                            generaEspais();

                            int link = t.getLink(name);

                            if (link != 0) {
                                String nameFileLink = t.getNameLinkedFile(name);
                                if (link == 1) {
                                    System.out.println("Es un enllaç fort i correspon al fitxer " + nameFileLink);
                                }
                                if (link == 2) {
                                    System.out.println("Es un enllaç simbolic i correspon al fitxer " + nameFileLink);
                                }
                            } else {
                                System.out.println("El fitxer introduit es un fitxer normal.");
                            }
                        } else {
                            System.out.println("Abans de poder fer aquesta comanda, s'ha de carregar el fitxer tar a la memoria.");
                        }
                        break;
                    //Un help per explicar que fa cada comanda
                    case 9:
                        System.out.println("Mostrant ajuda...");
                        break;
                    //Comanda per sortir del programa
                    case 10:
                        System.out.println("Sortint...");
                        status = false;
                        break;
                }
                System.out.println("----------------------------");
            }

        } else {
            System.out.println("No s'ha pogut trobar el fitxer tar o ha ocurregut un error amb ell.");
        }


    }

    private static void generaEspais() {
        for (int i = 0; i < 30; i++) {
            System.out.println("");
        }
    }


}
