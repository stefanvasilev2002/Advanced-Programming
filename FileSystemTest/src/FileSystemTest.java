import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class FileSystemTest {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());




    }
}
interface IFile extends Comparable<IFile>{
    String getFileName();
    long getFileSize();
    String getFileInfo();
    void sortBySize();
    long findLargestFile ();
    @Override
    default int compareTo(IFile o) {
        return Long.compare(getFileSize(), o.getFileSize());
    }
}
class File implements IFile{
    private String name;
    private long size;
    public File(String part, long parseLong) {
        name=part;
        size=parseLong;
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public String getFileInfo() {
        return String.format("\tFile name: %10s File size: %10d", name, size);
    }

    @Override
    public String toString() {
        return String.format("\tFile name: %10s File size: %10d\n", name, size);

    }

    @Override
    public void sortBySize() {
    }

    @Override
    public long findLargestFile() {
        return size;
    }
}
class Folder implements IFile{
    private String name;
    private long size;
    List<IFile> files;

    public Folder(String s) {
        this.name =s;
        this.size =0;
        this.files =new ArrayList<>();
    }

    public void addFile (IFile file) throws FileNameExistsException {
        for(int i=0; i<files.size(); i++){
            IFile temp=files.get(i);
            if(file.getFileName().equals(temp.getFileName())){
                throw new FileNameExistsException(file.getFileName(), name);
            }
        }
        files.add(file);
        size+=file.getFileSize();
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public void sortBySize() {
        files.sort(Comparator.naturalOrder());
    }
    @Override
    public String getFileInfo() {
        return String.format("\tFolder name: %10s Folder size: %10d\n", name, size);
    }
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(getFileInfo());
        for(int i=0; i<files.size(); i++){
            sb.append(files.get(i).getFileInfo());
        }
        return sb.toString();
    }

    @Override
    public long findLargestFile() {
        files.sort(Comparator.naturalOrder());
        for (int i=files.size()-1; i>=0; i--){
            IFile temp=files.get(i);
            if(temp instanceof File){
                return temp.getFileSize();
            }
        }
        return 0;
    }
}
class FileSystem{
    private Folder rootDirectory;
    public FileSystem(){
        rootDirectory=new Folder("root");
    }
    public void addFile (IFile file) throws FileNameExistsException {
        rootDirectory.addFile(file);
    }
    public long findLargestFile (){
        return rootDirectory.findLargestFile();
    }
    public void sortBySize(){
        rootDirectory.sortBySize();
    }
    public String getFileInfo() {
        return String.format("Folder name: %10s Folder size: %10d\n", rootDirectory.getFileName(), rootDirectory.getFileSize());
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(getFileInfo());
        for(int i=0; i<rootDirectory.files.size(); i++){
            sb.append(rootDirectory.files.get(i).toString());
        }
        return sb.toString();
    }
}
class FileNameExistsException extends Exception{
    public FileNameExistsException(String nameFile, String nameFolder) {
        System.out.println(String.format("There is already a file named %s in the folder %s", nameFile, nameFolder));
    }
}