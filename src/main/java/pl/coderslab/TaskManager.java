package pl.coderslab;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {

    static final String Nazwa_pliku = "task.csv";
    static final String[] akcja = {"add", "remove", "list", "exit"};
    static String[][] tasks;




    public static void main(String[] args) {
       tasks = wczytanieDanychZPliku(Nazwa_pliku);
        wyswietlanieOpcji(akcja);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String input = scanner.nextLine();
            switch (input) {
                case "exit":
                    exit_zapisIWyjscie(Nazwa_pliku,tasks);
                    System.out.println(ConsoleColors.RED + "Bye, bye");
                    System.exit(0);
                    break;
                case "add":
                    add_dodawanieZadania();
                    break;
                case "list":
                    list_WyswietlanieListy(tasks);
                    break;
                case "remove":
                    remove_usuniecieZadania(tasks,pobranieLiczby());
                    System.out.println("Value was successfully deleted");
                    break;
                default:
                    System.out.println("Please select a correct option"+Arrays.toString(akcja));
            }

        }

    }


    public static void exit_zapisIWyjscie (String fileName, String[][] tab){
        Path path = Paths.get(fileName);

        String[] lines = new String[tasks.length];
        for (int i = 0; i < tab.length; i++){
            lines[i] = String.join(",",tab[i]);
        }

        try {
            Files.write(path, Arrays.asList(lines));
        } catch (IOException e){
            e.printStackTrace();}

    }

    private static void add_dodawanieZadania(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please add task description");
        String description = scanner.nextLine();
        System.out.println("Please add task due date");
        String dueDate = scanner.nextLine();
        System.out.println("Is your task important: true/false");
        String isImportant = scanner.nextLine();

        tasks = Arrays.copyOf(tasks,tasks.length+1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = description;
        tasks[tasks.length - 1][1] = dueDate;
        tasks[tasks.length - 1][2] = isImportant;
    }

    public static void list_WyswietlanieListy (String[][] tab){
        try {
            for (int i = 0; i < tab.length; i++) {
                System.out.print(i + " : ");
                for (int j = 0; j < tab[i].length; j++) {
                    System.out.print(tab[i][j] + " ");
                }
                System.out.println();
            }
        }catch (NullPointerException e){
            System.out.println("Empty list");
        }
    }

    public static void remove_usuniecieZadania (String[][] tab, int index){
        try{
            if (index <tab.length){
                tasks = ArrayUtils.remove(tab,index);
            }
        } catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("Element not exist in tab");
        }
    }



    public static void wyswietlanieOpcji (String [] tab){
        System.out.println(ConsoleColors.BLUE + "Please select an option"+ConsoleColors.RESET);
        System.out.println(Arrays.toString(tab));
    }

    public static String[][] wczytanieDanychZPliku (String nazwaPliku ){
        Path path = Paths.get(nazwaPliku);
        if(!Files.exists(path)){
            System.out.println("File not exist.");
            System.exit(0);
        }
        String[][] tab = null;
        try {
            List<String > strings = Files.readAllLines(path);
            tab = new String[strings.size()][strings.get(0).split(",").length]; //strings.get(0).split(",").length - jest to ilość elementów pojedynczego wiersza

            for (int i = 0; i < strings.size(); i++) {
                String[] split = strings.get(i).split(",");
                for (int j = 0; j < split.length; j++) {
                    tab[i][j] = split[j];
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
      return tab;
    }

    public static boolean czyLiczbaWiekszRownaZero (String input){
        if (NumberUtils.isParsable(input)){
            return Integer.parseInt(input) >=0;
        }
        return false;
    }

    public static int pobranieLiczby(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select number to remove");

        String n = scanner.nextLine();
        while (!czyLiczbaWiekszRownaZero(n)){
            System.out.println("Incorrect argument passed. Please give number greater or equal 0");
            scanner.nextLine();
        }
        return Integer.parseInt(n);
    }
}
