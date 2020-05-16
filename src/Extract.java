import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Scanner;

public class Extract {
    public static void main(String[] args) throws IOException {
        Tar t = new Tar("C:\\Users\\samue\\Downloads\\tar\\archive2.tar");
        extractTar(t);
    }

    static void extractTar(Tar t) throws IOException {
        t.expand();
        Scanner sc = new Scanner(System.in);
        System.out.print("Introduiex la ruta a on vols extreure els fitxers del Tar: (en blanc si vols extreure'ls en el directori a on estas): ");
        String path = sc.nextLine();

        File f = new File(path);
        File f2 = new File(".");

        String tarName = t.getFileName().split("\\.")[0];
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
        }

        if (createFolder) {
            System.out.println("S'ha creat la carpeta correctament...");
            String[] fitxers = t.list();

            for (String fitxer : fitxers) {
                byte[] contentActualFile = t.getBytes(fitxer);
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
        }


    }
}
