import java.io.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class Tar {
    private String pathName;
    private String fileName;
    private long size;
    private CustomFile[] files;
    private boolean isExpanded;
    private boolean exists;

    // Constructor
    public Tar(String filename) {
        File f = new File(filename);

        if (f.exists()) {
            this.pathName = f.getPath();
            this.fileName = f.getName();
            this.size = (int) f.length();
            this.isExpanded = false;
        }
        this.exists = f.exists();
    }

    // Torna un array amb la llista de fitxers que hi ha dins el TAR
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

    // Torna un array de bytes amb el contingut del fitxer que té per nom
// igual a l'String «name» que passem per paràmetre
    public byte[] getBytes(String name) throws IOException {
        if (this.isExpanded) {
            for (CustomFile cf : this.files) {
                if (cf.getFileName().equals(name)) {
                    return cf.getContent();
                }
            }
        }
        return null;
    }

    // Expandeix el fitxer TAR dins la memòria
    public void expand() {
        List<CustomFile> filesList = new ArrayList<>();
        try {
            InputStream is = new FileInputStream(this.pathName);
            DataInputStream dis = new DataInputStream(is);

            while (true) {
                String nameFile = new String(dis.readNBytes(100)).trim();
                if (nameFile.equals("")) {
                    break;
                }
                String fileMode = new String(dis.readNBytes(8)).trim();
                //long fileModeBienHecho = dis.readLong();

                String ownerNumberUser = new String(dis.readNBytes(8)).trim();
                //long ownerNumberUserBienHecho = dis.readLong();

                String groupNumberUser = new String(dis.readNBytes(8)).trim();
                String sizeFile = new String(dis.readNBytes(12)).trim();
                String lastModification = new String(dis.readNBytes(12)).trim();
                String checksum = new String(dis.readNBytes(8)).trim();

                boolean isLink;
                if (new String(dis.readNBytes(1)).trim().equals(1)) {
                    isLink = true;
                } else {
                    isLink = false;
                }

                //String linkIndicator = new String(dis.readNBytes(1)).trim();
                String nameLinkedFile = new String(dis.readNBytes(100)).trim();
                dis.skipBytes(255);

                int sizeInDecimal = Integer.parseInt(sizeFile, 8);
                int seguentFitxer = (int) (Math.ceil(sizeInDecimal / 512.0) * 512);
                int addBytes = seguentFitxer - sizeInDecimal;

                byte[] content = dis.readNBytes(sizeInDecimal);
                dis.skipBytes(addBytes);
                filesList.add(new CustomFile(nameFile, fileMode, Integer.parseInt(ownerNumberUser), Integer.parseInt(groupNumberUser), Long.parseLong(sizeFile), lastModification, Integer.parseInt(checksum), isLink, nameLinkedFile, content));
            }

            this.files = new CustomFile[filesList.size()];
            filesList.toArray(this.files);

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.isExpanded = true;
    }

    public boolean extractTar(String path) throws IOException {
        boolean isExtracted = false;
        File f = new File(path);
        File f2 = new File(".");
        String tarName = this.getFileName().split("\\.")[0];
        boolean createFolder = false;

        if (path.equals("") && f2.exists() && f2.isDirectory() && f2.canWrite()) {
            int i = 1;
            while (new File(tarName).exists()) {
                tarName = tarName + i;
            }
            createFolder = new File(tarName).mkdirs();
        } else if (f.exists() && f.isDirectory() && f.canWrite()) {
            int i = 1;
            while (new File(f.getPath() + "\\" + tarName).exists()) {
                tarName = tarName + i;
            }
            System.out.println(tarName);
            createFolder = new File(f.getPath() + "\\" + tarName).mkdirs();
        } else {
            return false;
        }

        if (createFolder) {

            System.out.println("S'ha creat la carpeta correctament...");
            String[] fitxers = list();

            for (String fitxer : fitxers) {
                byte[] contentActualFile = getBytes(fitxer);
                OutputStream os;
                if (path.equals("")) {
                    os = new FileOutputStream(f2.getAbsoluteFile() + "\\" + tarName + "\\" + fitxer);
                } else {
                    os = new FileOutputStream(f.getPath() + "\\" + tarName + "\\" + fitxer);
                }

                os.write(contentActualFile);
                os.close();
            }
        } else {
            System.out.println("No s'ha pogut crear la carpeta");
            return false;
        }
        return true;
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
}

class CustomFile {
    private String fileName;
    private String fileMode;
    private int ownerID;
    private int groupID;
    private long fileSize;
    private String lastModification;
    private int checksum;
    private boolean isLink;
    private String nameLinkedFile;
    private byte[] content;


    public CustomFile(String fileName, String fileMode, int ownerID, int groupID, long fileSize, String lastModification, int checksum, boolean isLink, String nameLinkedFile, byte[] content) {
        this.fileName = fileName;
        this.fileMode = fileMode;
        this.ownerID = ownerID;
        this.groupID = groupID;
        this.fileSize = fileSize;
        this.lastModification = lastModification;
        this.checksum = checksum;
        this.isLink = isLink;
        this.nameLinkedFile = nameLinkedFile;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileMode() {
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

    public boolean isLink() {
        return isLink;
    }

    public String getNameLinkedFile() {
        return nameLinkedFile;
    }

    public byte[] getContent() {
        return content;
    }
}