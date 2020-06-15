import java.io.*;
import java.io.File;
import java.sql.Timestamp;
import java.util.*;

class Tar {
    private String pathName;
    private String fileName;
    private long size;
    private CustomFile[] files;
    private boolean isExpanded;
    private boolean isTar;
    private boolean exists;

    // Constructor
    public Tar(String filename) {
        File f = new File(filename);

        //Nomes si el fitxer existeix, inicialitzarem tots els atributs de la classe Tar gràcies a les dades que ens donen els metodes de la classe File
        if (f.exists()) {
            this.pathName = f.getPath();
            this.fileName = f.getName();
            this.size = (int) f.length();
            this.isExpanded = false;
            this.isTar = this.fileName.contains(".tar");
        }

        this.exists = f.exists();
    }

    //Torna un array amb la llista de fitxers que hi ha dins el TAR
    public String[] list() {
        String[] filesName = new String[this.files.length];
        if (this.isExpanded) {
            int i = 0;
            for (CustomFile cf : this.files) {
                filesName[i] = cf.getFileName();
                i++;
            }
        }
        return filesName;
    }

    //Torna un array de bytes amb el contingut del fitxer que té per nom
    // igual a l'String «name» que passem per paràmetre
    public byte[] getBytes(String name) {
        if (this.isExpanded) {
            for (CustomFile cf : this.files) {
                if (cf.getFileName().equals(name)) {
                    return cf.getContent();
                }
            }
        }
        return null;
    }

    //Torna un string amb la ultima data de modificacio del fitxer. Com a parametre s'ha d'enviar el nom del fitxer.
    public String getLastModification(String name) {
        if (this.isExpanded) {
            for (CustomFile cf : this.files) {
                if (cf.getFileName().equals(name)) {
                    long timestamp = Integer.parseInt(cf.getLastModification(), 8);
                    Timestamp tt = new Timestamp(timestamp * 1000);
                    Date date = new Date(tt.getTime());
                    return date.toString();
                }
            }
        }
        return null;
    }

    //Retorna un long amb el tamany d'un fitxer del tar en concret. Com a parametre s'ha de passar el nom del fitxer
    public long getSize(String name) {
        for (CustomFile cf : this.files) {
            if (cf.getFileName().equals(name)) {
                return cf.getFileSize();
            }
        }
        return -1;
    }

    //Retorna un array de int de dues posicions en la que ens dona quin es el ID propietari i el ID de grup respectivament, d'un fitxer en concret. Com els altres metodes, s'ha de passar per parametre un nom.
    public int[] getIds(String name) {
        int[] ids = new int[2];
        ids[0] = -1;
        ids[1] = -1;
        for (CustomFile cf : this.files) {
            if (cf.getFileName().equals(name)) {
                ids[0] = cf.getOwnerID();
                ids[1] = cf.getGroupID();
            }
        }
        return ids;
    }

    //Aquest metode ens permet aconseguir quins son els permisos d'un fitxer en concret. S'ha de passar un nom.
    //Cal dir que ho ens retornara en format numeric (755) i no amb lletres (rwx) o altres.
    public int getPermissions(String name) {
        for (CustomFile cf : this.files) {
            if (cf.getFileName().equals(name)) {
                return cf.getFileMode();
            }
        }
        return -1;
    }

    //Aquest metode ens proporciona si un fitxer en concret del tar es un link simbolic o no. Retornara 0 si es un fitxer normal, 1 si es un hard link i el 2 si es un enllaç simbolic.
    public int getLink(String name) {
        for (CustomFile cf : this.files) {
            if (cf.getFileName().equals(name)) {
                return cf.link();
            }
        }
        return -1;
    }


    //Metode que ens permet obtenir el nom del arxiu a on hi apunta un arxiu amb link simbolic o hard. S'ha de passar el nom del fitxer al que volem comprovar.
    public String getNameLinkedFile(String name) {
        for (CustomFile cf : this.files) {
            if (cf.getFileName().equals(name)) {
                return cf.getNameLinkedFile();
            }
        }

        return null;
    }


    //Metode que ens permet extreure tots els fitxers que hi ha de dins d'un fitxer tar. S'ha de passar per parametre el path a on es vol extreure.
    //Aquest metode retorna un boolean indicant si el process d'extració ha sortit correctament (true) o per si al contrari no s'ha pogut fer o ha ocurregut qualsevol error(false)
    public boolean extractTar(String path, boolean makeFolder) {

        //Cream un objecte File de més, per poder aconseguir quina es la ruta relativa d'execució en cas de que l'usuari volgui descomprimir en el mateix lloc a on s'executa el programa
        File f = new File(path);
        File f2 = new File(".");

        if (!f.exists() || !f2.exists()) {
            return false;
        }
        //Feim un split per tal d'aconseguir el nom del tar sense l'extensio. Això ho feim per crear la carpeta nomes amb el nom
        String folderName = this.getFileName().split("\\.")[0];
        int tarNameLength = folderName.length();
        boolean createFolder = false;

        //Si l'usuari vol crear una carpeta a on guardar el contingut del TAR haura de pasr per aquesta condicio, si no es obviara
        if (makeFolder) {
            //Si el path que ha introduit l'usuari es buit, voldra dir que vol descomprimir les dades en el directori a on s'executa el programa i crearem la corresponent carpeta en aquest path
            if (path.equals("") && f2.isDirectory() && f2.canWrite()) {

                //El metode checIfFolderExists ens permet aconseguir quin sera el nom amb el que haurem de crear la carpeta. Si ha moltes carpetes iguals,
                // aquest metode s'encarrega de crear un carpeta amb un nom totalment diferent
                folderName = checkIfFolderExists("", folderName, tarNameLength);
                createFolder = new File(folderName).mkdirs();

                //En canvi si ingressa un path haurem de crear la carpeta en el path que ha especificat l'usuari
            } else if (!path.equals("") && f.isDirectory() && f.canWrite()) {

                folderName = checkIfFolderExists(f.getPath(), folderName, tarNameLength);

                //I cream la carpeta amb el name corresponent
                createFolder = new File(f.getPath() + "\\" + folderName).mkdirs();
                //S'hi no es cap de les anteriors i l'usuari ha seleccionat crear una carpeta, doncs retornarem false indicant que el proces de creacio de la carpeta no ha anat be.
            } else {
                return false;
            }
        }

        //Si es dona el cas que l'usuari vol crear una carpeta pero el boolea que ens permet saber si s'ha creat correctament la carpeta es troba en false, doncs haurem de retornar false.
        //Si no es crea la carpeta correctament evitarem seguir amb la part de descomprimir.
        if (!createFolder && makeFolder) {
            return false;
        }

        String[] fitxers = list();

        for (String fitxer : fitxers) {
            byte[] contentActualFile = getBytes(fitxer);

            try {
                OutputStream os;

                //Depen que hagui introduit l'usuari, treballarem amb el path sencer o amb el absoluteFile. Tambe canviara si l'usuari vol crear la carpeta o no, s'ha de canviar la ruta.
                if (path.equals("")) {
                    if (makeFolder) {
                        os = new FileOutputStream(f2.getAbsoluteFile() + "\\" + folderName + "\\" + fitxer);
                    } else {
                        os = new FileOutputStream(f2.getAbsoluteFile() + "\\" + fitxer);
                    }
                } else {
                    if (makeFolder) {
                        os = new FileOutputStream(f.getPath() + "\\" + folderName + "\\" + fitxer);
                    } else {
                        os = new FileOutputStream(f.getPath() + "\\" + fitxer);
                    }
                }
                os.write(contentActualFile);
                os.close();
            } catch (FileNotFoundException e) {
                System.out.println("No s'ha trobat el fitxer.");
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                System.out.println("Ha ocorregut un error amb el fitxer.");
                e.printStackTrace();
                return false;
            } catch (Exception ex) {
                System.out.println("Ha ocorregut un error desconegut");
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }


    //Expandeix el fitxer TAR dins la memòria
    public void expand() {
        List<CustomFile> filesList = new ArrayList<>();
        try {
            InputStream is = new FileInputStream(this.pathName);
            DataInputStream dis = new DataInputStream(is);

            //Bucle per recorrer el header i el content de cada un dels fitxers que hi ha al tar
            while (true) {
                String nameFile = new String(dis.readNBytes(100)).trim();

                //En el moment que la variable nameFile sigui un string buit, voldra dir que ja es pot acabar el bucle perque no hi ha mes fitxers a recorrer
                if (nameFile.equals("")) {
                    break;
                }

                //Anem conseguint totes les dades que ens dona el corresponent header i al mateix temps anem passant de bytes gràcies a readNBytes.
                int fileMode = Integer.parseInt(new String(dis.readNBytes(8)).trim());
                String ownerNumberUser = new String(dis.readNBytes(8)).trim();
                String groupNumberUser = new String(dis.readNBytes(8)).trim();
                String sizeFile = new String(dis.readNBytes(12)).trim();
                String lastModification = new String(dis.readNBytes(12)).trim();
                String checksum = new String(dis.readNBytes(8)).trim();
                String link = new String(dis.readNBytes(1)).trim();
                String nameLinkedFile = new String(dis.readNBytes(100)).trim();

                //Avançam 255 per acabar el header i passar al content del fitxer
                dis.skipBytes(255);

                //Important passar el tamany que ens dona el header de octal a decimal.
                int sizeInDecimal = Integer.parseInt(sizeFile, 8);

                //Aconseguim el numero que ens permetra passar al següent header i obviar el bytes sobrants del content. Això passa per que els blocs van de 512 en 512 bytes.
                int seguentFitxer = (int) (Math.ceil(sizeInDecimal / 512.0) * 512);
                int addBytes = seguentFitxer - sizeInDecimal;

                byte[] content = dis.readNBytes(sizeInDecimal);

                //Feim un skip dels bytes que no volem i cream un objecte de tipus CustomFile amb les dades que hem aconseguit previament.
                //Com que aquest proces es farà tantes vegades com fitxers hi hagui, haurem de guardar-los en una llista.
                dis.skipBytes(addBytes);
                filesList.add(new CustomFile(nameFile, fileMode, Integer.parseInt(ownerNumberUser), Integer.parseInt(groupNumberUser), sizeInDecimal, lastModification, Integer.parseInt(checksum), Integer.parseInt(link), nameLinkedFile, content));
            }

            //Inicialitzam el array a on contindra tots els fitxers del tar i passam la llista amb tots els el CustomFiles a un array.
            this.files = new CustomFile[filesList.size()];
            filesList.toArray(this.files);

            //Important posar l'atribut isExpanded a true, indicant que s'ha carregat a la memoria el fitxer TAR.
            this.isExpanded = true;

            //I no oblidar-se de tancar el fluxe de dades tant del dataInputStream com del InputStream
            dis.close();
            is.close();
        } catch (FileNotFoundException e) { //Capturam les possibles excepcions
            System.out.println("No s'ha pogut trobar el tar indicat.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Ha ocorregut un error amb el tar.");
            e.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Ha ocorregut un error desconegut");
        }
    }

    private String checkIfFolderExists(String path, String tarName, int tarNameLength) {
        int i = 1;

        if (!path.equals("")) {
            path = path + "\\";
        }

        while (new File(path + tarName).exists()) {
            if (i > 1) {
                StringBuilder s = new StringBuilder();
                for (int j = 0; j < tarNameLength; j++) {
                    s.append(tarName.charAt(j));
                }
                tarName = s.toString();
            }
            tarName = tarName + "(" + i + ")";
            i++;
        }
        return tarName;
    }

    public String getFileName() {
        return fileName;
    }

    public long getSize() {
        return size;
    }

    public CustomFile[] getFiles() {
        return files;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public boolean isExists() {
        return exists;
    }

    public boolean isTar() {
        return isTar;
    }
}

class CustomFile {
    private String fileName; //X
    private int fileMode; //X
    private int ownerID; //X
    private int groupID; //X
    private long fileSize; //X
    private String lastModification; //X
    private int checksum;
    private int link; //X
    private String nameLinkedFile;//X
    private byte[] content; //X


    //Classe que ens permet representar el que seria un fitxer qualsevol en un tar. Tots els seus atributs ho podem trobar en el header
    public CustomFile(String fileName, int fileMode, int ownerID, int groupID, long fileSize, String lastModification, int checksum, int link, String nameLinkedFile, byte[] content) {
        this.fileName = fileName;
        this.fileMode = fileMode;
        this.ownerID = ownerID;
        this.groupID = groupID;
        this.fileSize = fileSize;
        this.lastModification = lastModification;
        this.checksum = checksum;
        this.link = link;
        this.nameLinkedFile = nameLinkedFile;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public int getFileMode() {
        return fileMode;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public int getGroupID() {
        return groupID;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getLastModification() {
        return lastModification;
    }

    public int getChecksum() {
        return checksum;
    }

    public int link() {
        return link;
    }

    public String getNameLinkedFile() {
        return nameLinkedFile;
    }

    public byte[] getContent() {
        return content;
    }
}