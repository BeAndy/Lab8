import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andrew on 12/16/2016.
 */
public class Runner {
    public static void main(String[] args) {
        List<Juice> juiceList = getJuiceList("Juice.txt");
        List<String> uniqueIngredients = getUniqueIngredients(juiceList);
        writeData("juice1.txt", uniqueIngredients.iterator());
        Thread compThread = new Thread(() ->
                Collections.sort(uniqueIngredients, (first, second) -> (first.charAt(0) > second.charAt(0) ? 1 : -1)));
        compThread.run();
        writeData("juice2.txt", uniqueIngredients.iterator());
        getWashNumber(juiceList);
        writeIntData("juice3.txt", getWashNumber(juiceList));
        System.out.println("Done!");
    }

    private static int getWashNumber(List<Juice> juiceList) {
        int washNumber = 1; /*At least in the end*/
        for (int i = 0; i < juiceList.size() - 1; i++) {
            Juice currentJuice = juiceList.get(i);
            Juice nextJuice = juiceList.get(i + 1);
            boolean isUnique = true;
            for (int j = 0; j < currentJuice.ingredients.size(); j++) {
                for (int k = 0; k < nextJuice.ingredients.size(); k++) {
                    if (currentJuice.ingredients.get(j).compareTo(nextJuice.ingredients.get(k)) == 0) {
                        isUnique = false;
                    }
                }
            }
            if (isUnique) {
                washNumber++;
            }
        }
        return washNumber;
    }

    public static void printUniqueIngredients(List<String> juiceList) {
        System.out.println("\n");
        for (String currentIngredients : juiceList) {
            System.out.println(currentIngredients + " ");
        }
    }

    private static void writeData(String way, Iterator<String> iterator) {
        try {
            FileWriter writer = new FileWriter(new File(way));
            while (iterator.hasNext()) {
                writer.write(iterator.next());
                writer.write(System.lineSeparator());
            }
            writer.close();
        } catch (IOException exception) {
            exception.getLocalizedMessage();
        }
    }

    private static void writeIntData(String way, int number) {
        try {
            FileWriter writer = new FileWriter(new File(way));
            writer.write(number + " ");
            writer.close();
        } catch (IOException exception) {
            exception.getLocalizedMessage();
        }
    }

    public static List<String> getUniqueIngredients(List<Juice> juiceList) {
        List<String> uniqueIngredients = new ArrayList<>();
        for (Juice currentJuice : juiceList) {
            for (String currentIngredient : currentJuice.ingredients) {
                if (uniqueIngredients.isEmpty()) {
                    uniqueIngredients.add(currentIngredient);
                } else {
                    boolean isUnique = true;
                    for (int i = 0; i < uniqueIngredients.size(); i++) {
                        String currentUniqueIngredient = uniqueIngredients.get(i);
                        if (currentIngredient.compareTo(currentUniqueIngredient) == 0) {
                            isUnique = false;
                        }
                    }
                    if (isUnique) {
                        uniqueIngredients.add(currentIngredient);
                    }
                }
            }
        }
        return uniqueIngredients;
    }

    public static void printJuiceList(List<Juice> juiceList) {
        for (Juice currentJuice : juiceList) {
            System.out.println("\n");
            for (String currentIngredient : currentJuice.ingredients) {
                System.out.println(currentIngredient + " ");
            }
        }
    }

    public static Juice getJuiceComponents(String components) {
        Juice currentJuice = new Juice();
        Pattern pattern = Pattern.compile("\\w+\\s");
        Matcher matcher = pattern.matcher(components);
        while (matcher.find()) {
            currentJuice.ingredients.add(matcher.group());
        }
        return currentJuice;
    }

    public static List<Juice> getJuiceList(String way) {
        List<Juice> juiceList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(way)));
            String tempString = br.readLine();
            while (tempString != null) {
                Juice currentJuice = getJuiceComponents(tempString + " ");
                juiceList.add(currentJuice);
                tempString = br.readLine();
            }
            br.close();
        } catch (IOException currentException) {
            currentException.getLocalizedMessage();
        }
        return juiceList;
    }
}
