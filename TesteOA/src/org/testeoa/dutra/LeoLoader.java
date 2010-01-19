package org.testeoa.dutra;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class LeoLoader {
	
	private ZipClassLoader zipLoader = null;
    private JFileChooser fileChooser = null;
    private File file = null;
    private ArrayList<Unidade> lista = null;
    String jarName = null;
    
    public LeoLoader() {
        lista = new ArrayList<Unidade>();
    }
    
    public void loadJar() {
    	file = getFile();
    	if (file == null) {
            System.out.println("não conseguiu carregar o arquivo!!!");
            System.exit(0);
        }
    	
        try {
            zipLoader = new ZipClassLoader(file.getPath());
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        Enumeration<? extends ZipEntry> list = zipLoader.getZipFile().entries();
        ZipEntry entry;
        
        String classPackage;
        String className;
        String classURL;
        jarName = file.getName();

        while (list.hasMoreElements()) {
            entry = list.nextElement();
            if (entry.toString().toLowerCase().endsWith(".class") && (!entry.toString().toLowerCase().contains("$")) ) {
                classPackage = entry.getName().substring(0, entry.getName().lastIndexOf("/") + 1);
                className = entry.getName().substring(entry.getName().lastIndexOf("/") + 1);
                classURL = classPackage.replace('/', '.') + className.substring(0, className.lastIndexOf('.'));
                lista.add(new Unidade(classPackage, className, classURL));
            }
        }
        
        try {
			ClassPathHacker.addFile(file);
		} catch (IOException e) {
			
		}
        
    }
    
    public void loadJar(String fileName) {
    	file = new File(fileName);
    	if (file == null) {
            System.out.println("não conseguiu carregar o arquivo!!!");
            System.exit(0);
        }
    	
        try {
            zipLoader = new ZipClassLoader(file.getPath());
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        Enumeration<? extends ZipEntry> list = zipLoader.getZipFile().entries();
        ZipEntry entry;
        
        String classPackage;
        String className;
        String classURL;
        jarName = file.getName();

        while (list.hasMoreElements()) {
            entry = list.nextElement();
            if (entry.toString().toLowerCase().endsWith(".class") && (!entry.toString().toLowerCase().contains("$")) ) {
                classPackage = entry.getName().substring(0, entry.getName().lastIndexOf("/") + 1);
                className = entry.getName().substring(entry.getName().lastIndexOf("/") + 1);
                classURL = classPackage.replace('/', '.') + className.substring(0, className.lastIndexOf('.'));
                lista.add(new Unidade(classPackage, className, classURL));
            }
        }
        
        try {
			ClassPathHacker.addFile(file);
		} catch (IOException e) {
			
		}
        
    }
    
    public File getFile() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.setApproveButtonText("Escolher");
            fileChooser.setApproveButtonToolTipText("Incluir este este JAR ...");
            fileChooser.setApproveButtonMnemonic('E');
            fileChooser.setDialogTitle("Escolha um JAR para o projeto ...");
        }

        fileChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }

                if (f.getName().toLowerCase().endsWith(".jar")) {
                    return true;
                } else {
                    return false;
                }

            }

            @Override
            public String getDescription() {
                return "*.jar";
            }
        });
        fileChooser.showOpenDialog(null);
        return fileChooser.getSelectedFile();
    }

    public ArrayList<Unidade> getLista() {
		return lista;
	}
    
    public String getFileName() {
    	return file.getName();
    }
    
    public File getFileFile(){
    	return this.file;
    }
    
}
