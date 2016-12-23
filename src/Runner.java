import java.io.*;
import java.util.*;
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
        writeIntData("juice3.txt", getWashNumber(juiceList));
        System.out.println("Done!");
    }

    private static int getWashNumber(List<Juice> juiceList) {
       /* printJuiceList(juiceList);
        System.out.println("_________________");*/
        Collections.sort(juiceList, (first, second) -> {
            Collections.sort(first.ingredients);
            Collections.sort(second.ingredients);
            if (first.ingredients.get(0).charAt(0) == second.ingredients.get(0).charAt(0)) {
                return first.ingredients.size() > second.ingredients.size() ? 1 : -1;
            }
            return first.ingredients.get(0).charAt(0) > second.ingredients.get(0).charAt(0) ? 1 : -1;
        });

        int washNumber = 1; /*At least in the end*/
        printJuiceList(juiceList);
        for (int i = 0; i < juiceList.size() - 1; i++) {
            Juice currentJuice = juiceList.get(i);
            Juice nextJuice = juiceList.get(i + 1);
            if (!Objects.equals(currentJuice.ingredients.get(0), nextJuice.ingredients.get(0))) {
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

    private static List<String> getUniqueIngredients(List<Juice> juiceList) {
        List<String> uniqueIngredients = new ArrayList<>();
        for (Juice currentJuice : juiceList) {
            for (String currentIngredient : currentJuice.ingredients) {
                if (uniqueIngredients.isEmpty()) {
                    uniqueIngredients.add(currentIngredient);
                } else {
                    boolean isUnique = true;
                    for (String currentUniqueIngredient : uniqueIngredients) {
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

    private static void printJuiceList(List<Juice> juiceList) {
        for (Juice currentJuice : juiceList) {
            for (String currentIngredient : currentJuice.ingredients) {
                System.out.println(currentIngredient + " ");
            }
            System.out.println("\n");
        }
    }

    private static Juice getJuiceComponents(String components) {
        Juice currentJuice = new Juice();
        Pattern pattern = Pattern.compile("\\w+\\s");
        Matcher matcher = pattern.matcher(components);
        while (matcher.find()) {
            currentJuice.ingredients.add(matcher.group());
        }
        return currentJuice;
    }

    private static List<Juice> getJuiceList(String way) {
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
