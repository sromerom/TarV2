import java.io.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

class Tar {
    private String fileName;
    private long size;
    private CustomFile[] files;
    private boolean exists;

    // Constructor
    public Tar(String filename) {
        this.fileName = filename;

        File f = new File(this.fileName);
        this.exists = f.exists();
        this.size = (int) f.length();
    }

    // Torna un array amb la llista de fitxers que hi ha dins el TAR
    public String[] list() {
        return null;
    }

    // Torna un array de bytes amb el contingut del fitxer que té per nom
// igual a l'String «name» que passem per paràmetre
    public byte[] getBytes(String name) {
        return null;
    }

    // Expandeix el fitxer TAR dins la memòria
    public void expand() {
        List<CustomFile> filesList = new ArrayList<>();
        try {
            InputStream is = new FileInputStream(this.fileName);
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
                dis.skipBytes(seguentFitxer);

                System.out.println(nameFile);
                filesList.add(new CustomFile(nameFile, fileMode, Integer.parseInt(ownerNumberUser), Integer.parseInt(groupNumberUser), Long.parseLong(sizeFile), lastModification, Integer.parseInt(checksum), isLink, nameLinkedFile));
            }

            this.files = new CustomFile[filesList.size()];
            filesList.toArray(this.files);

        } catch (IOException e) {
            e.printStackTrace();
        }

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


    public CustomFile(String fileName, String fileMode, int ownerID, int groupID, long fileSize, String lastModification, int checksum, boolean isLink, String nameLinkedFile) {
        this.fileName = fileName;
        this.fileMode = fileMode;
        this.ownerID = ownerID;
        this.groupID = groupID;
        this.fileSize = fileSize;
        this.lastModification = lastModification;
        this.checksum = checksum;
        this.isLink = isLink;
        this.nameLinkedFile = nameLinkedFile;
    }
}