package vn.funix.fx21678.java.asm04.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class BinaryFileService {

    public static <T> List<T> readFile(String filename) {
        List<T> objects = new ArrayList<>();

        try (ObjectInputStream file = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            boolean eof = false;
            while (!eof) {
                try {
                    T object = (T) file.readObject();
                    objects.add(object);
                } catch (EOFException | ClassNotFoundException e) {
                    eof = true;
                }
            }
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
        return objects;
    }

    public static <T> void writeFile(String filename, List<T> objects) {
        try (ObjectOutputStream file = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
            for (T obj : objects) {
                file.writeObject(obj);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

}
