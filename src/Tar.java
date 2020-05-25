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

    public String getLastModification(String name) {
        if (this.isExpanded) {
            for (CustomFile cf : this.files) {
                if (cf.getFileName().equals(name) || cf.getFileName().contains(name)) {
                    long timestamp = Integer.parseInt(cf.getLastModification(), 8);
                    Timestamp tt = new Timestamp(timestamp * 1000);
                    Date date = new Date(tt.getTime());
                    return date.toString();
                }
            }
        }
        return null;
    }

    public boolean checkChecksum(String file1, String file2) {
        for (CustomFile cf : this.files) {
        }
        return true;
    }

    public long getSize(String name) {
        for (CustomFile cf : this.files) {
            if (cf.getFileName().equals(name)) {
                return cf.getFileSize();
            }
        }
        return 0;
    }

    public int[] getIds(String name) {
        int[] ids = new int[2];
        for (CustomFile cf : this.files) {
            if (cf.getFileName().equals(name)) {
                ids[0] = cf.getOwnerID();
                ids[1] = cf.getGroupID();
            }
        }
        return ids;
    }

    public int getPermissions(String name) {
        for (CustomFile cf : this.files) {
            if (cf.getFileName().equals(name)) {
                return cf.getFileMode();
            }
        }
        return 0;
    }

    public String getLink(String name) {
        for (CustomFile cf : this.files) {
            if (cf.getFileName().equals(name)) {
                return new String(cf.isLink() + " " + cf.getNameLinkedFile());
            }
        }

        return "";
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
                int fileMode = Integer.parseInt(new String(dis.readNBytes(8)).trim());
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
                filesList.add(new CustomFile(nameFile, fileMode, Integer.parseInt(ownerNumberUser), Integer.parseInt(groupNumberUser), sizeInDecimal, lastModification, Integer.parseInt(checksum), isLink, nameLinkedFile, content));
            }

            this.files = new CustomFile[filesList.size()];
            filesList.toArray(this.files);
            this.isExpanded = true;
            dis.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private String fileName; //X
    private int fileMode; //X
    private int ownerID; //X
    private int groupID; //X
    private long fileSize; //X
    private String lastModification; //X
    private int checksum;
    private boolean isLink;
    private String nameLinkedFile;
    private byte[] content; //X


    public CustomFile(String fileName, int fileMode, int ownerID, int groupID, long fileSize, String lastModification, int checksum, boolean isLink, String nameLinkedFile, byte[] content) {
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