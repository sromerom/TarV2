import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Benvingut a TARV2. Introduiex la ruta absoluta d'un fitxer TAR per començar a treballar amb ell: ");
        String pathFile = sc.nextLine();

        Tar t = new Tar(pathFile);
        boolean status = true;

        //Nomes passarem al menu de selecció de comandes si el fitxer que ens ha passat existeix i es un fitxer TAR.
        if (t.isExists()) {
            if (t.isTar()) {

                //Anirem carregant el menu amb un bucle fins que l'usuari sorti.
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
                    System.out.printf("%-15S\n", "9.-Exit");

                    System.out.println("##############################");
                    System.out.println();
                    System.out.print("Introduiex el numero que correspongui a la comanda que vols realitzar: ");
                    int comanda = sc.nextInt();

                    if (comanda < 1 || comanda > 9) {
                        System.out.println("Introduiex un numero valid, per favor...");
                    } else {
                        Scanner sc2;


                        switch (comanda) {
                            //Carregar fitxer a la memoria
                            case 1:
                                generaEspais();

                                t.expand();
                                System.out.println("Carregat TAR a la memoria.");
                                break;
                            //Llistam tots els fitxers
                            case 2:
                                if (t.isExpanded()) {
                                    generaEspais();

                                    String[] fitxers = t.list();
                                    for (int i = 0; i < fitxers.length; i++) {
                                        System.out.printf("%d.-%s\n", (i + 1), fitxers[i]);
                                    }
                                } else {
                                    System.out.println("Abans de poder fer aquesta comanda, s'ha de carregar el fitxer TAR a la memoria.");
                                }
                                break;
                            //Extreure tots els fitxers
                            case 3:
                                if (t.isExpanded()) {
                                    sc2 = new Scanner(System.in);
                                    System.out.print("Introduiex la ruta a on vols extreure els fitxers del Tar: (en blanc si vols extreure'ls en el directori a on estas): ");
                                    String path2 = sc2.nextLine();

                                    System.out.println("Ho vols extreure en una carpeta? (Y o N)");
                                    String opcioElegida = sc2.next();
                                    generaEspais();

                                    //Aquest boolean ens permetra saber si l'usuari vol crear una carpeta per quan es descomprimeixi el contigut del TAR o per si al contrari o vol al mateix directori directament.
                                    boolean makeFolder = false;

                                    //Si no introduiex les lletres Y o N, no es passar a extreure els arxius
                                    if (opcioElegida.length() != 1 || !opcioElegida.equalsIgnoreCase("N") && !opcioElegida.equalsIgnoreCase("Y")) {
                                        System.out.println("Introduiex nomes la opcio Y o N...");
                                    } else {

                                        if (opcioElegida.equals("Y")) {
                                            makeFolder = true;
                                        }

                                        boolean successfullyExtract = t.extractTar(path2, makeFolder);

                                        //Mostrarem un petit text descriptiu per informar a l'usuari s'hi el proces d'extracio ha sortir be o si ha ocurregut qualsevol error.
                                        if (successfullyExtract) {
                                            System.out.println("S'ha extret correctament");
                                        } else {
                                            System.out.println("No s'ha pogut extreure satisfactoriament");
                                        }
                                    }

                                } else {
                                    System.out.println("Abans de poder fer aquesta comanda, s'ha de carregar el fitxer TAR a la memoria.");
                                }
                                break;
                            //Aconseguir ultima data de modificacio
                            case 4:
                                if (t.isExpanded()) {
                                    sc2 = new Scanner(System.in);
                                    System.out.print("Introduiex el nom del fitxer que vols comprovar l'ultima data de modificació: ");
                                    String name = sc2.nextLine();
                                    generaEspais();

                                    //Si ens el metode ens retorna null, voldra dir que no ha trobat el fitxer en el array de CustomFiles.
                                    String lastModification = t.getLastModification(name);
                                    if (lastModification != null) {
                                        System.out.println(lastModification);
                                    } else {
                                        System.out.println("No s'ha trobat el fitxer introduit");
                                    }

                                } else {
                                    System.out.println("Abans de poder fer aquesta comanda, s'ha de carregar el fitxer TAR a la memoria.");
                                }
                                break;
                            //Obtenir tamany d'un fitxer en concret
                            case 5:
                                if (t.isExpanded()) {
                                    sc2 = new Scanner(System.in);
                                    System.out.print("Introduiex el nom del fitxer per mostrar el seu tamany: ");
                                    String name = sc2.nextLine();
                                    generaEspais();

                                    //Si ens el metode ens retorna -1, voldra dir que no ha trobat el fitxer en el array de CustomFiles.
                                    long size = t.getSize(name);
                                    if (size != -1) {
                                        System.out.printf("El tamany del fitxer es de %d Bytes\n", size);
                                    } else {
                                        System.out.println("No s'ha trobat el fitxer introduit");
                                    }

                                } else {
                                    System.out.println("Abans de poder fer aquesta comanda, s'ha de carregar el fitxer TAR a la memoria.");
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

                                    //Si ens el metode ens retorna un array amb valors -1, voldra dir que no ha trobat el fitxer en el array de CustomFiles.
                                    if (ids[0] == -1 || ids[1] == -1) {
                                        System.out.println("No s'ha trobat el fitxer introduit");
                                    } else {
                                        System.out.println("Owner ID: " + ids[0]);
                                        System.out.println("Group ID:" + ids[1]);
                                    }
                                } else {
                                    System.out.println("Abans de poder fer aquesta comanda, s'ha de carregar el fitxer TAR a la memoria.");
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

                                    if (permisos != -1) {
                                        System.out.println("Els permisos del fitxer són els següents: " + permisos);
                                    } else {
                                        System.out.println("No s'ha trobat el fitxer introduit");
                                    }

                                } else {
                                    System.out.println("Abans de poder fer aquesta comanda, s'ha de carregar el fitxer TAR a la memoria.");
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

                                    //En aquest cas, el que ens retorna el metode getLink no es nomes un sol numero indicant si ha anat be o no. Aquest metode retorna un total de 4 numeros indicant
                                    // diferents possibilitats de fitxers link.
                                    if (link == -1) {
                                        System.out.println("No s'ha trobat el fitxer introduit");
                                    } else {
                                        String nameFileLink = t.getNameLinkedFile(name);
                                        if (link == 0) {
                                            System.out.println("El fitxer introduit es un fitxer normal");
                                        } else if (link == 1) {
                                            System.out.println("Es un enllaç fort i correspon al fitxer " + nameFileLink);
                                        } else if (link == 2) {
                                            System.out.println("Es un enllaç simbolic i correspon al fitxer " + nameFileLink);
                                        }

                                    }
                                } else {
                                    System.out.println("Abans de poder fer aquesta comanda, s'ha de carregar el fitxer TAR a la memoria.");
                                }
                                break;
                            //Comanda que ens permet sortir del programa
                            case 9:
                                System.out.println("Sortint...");
                                status = false;
                                break;
                            default:
                                System.out.println("Ha hagut un error");

                        }
                    }

                    System.out.println("------------------------------------------------------------");
                }
            } else {
                System.out.println("Ho sentim, aquest programa nomes treballa amb arxius TAR.");
            }

        } else {
            System.out.println("No s'ha pogut trobar el fitxer TAR o ha ocurregut un error amb ell.");
        }


    }

    private static void generaEspais() {
        for (int i = 0; i < 30; i++) {
            System.out.println("");
        }
    }


}
