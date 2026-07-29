package Io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HeroMain {
    public static void main(String[] args) {
        String readPath = "src/Io/heros.txt";
        String writePath = "src/Io/heros2.txt";

        ArrayList<Hero> heroList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(readPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                Hero hero = new Hero(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3]);
                heroList.add(hero);
            }
        } catch (IOException e) {
            System.err.println("读取文件失败：" + e.getMessage());
            e.printStackTrace();
            return;
        }

        Collections.sort(heroList, new Comparator<Hero>() {
            @Override
            public int compare(Hero h1, Hero h2) {
                return h2.getAge() - h1.getAge();
            }
        });

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(writePath))) {
            for (Hero hero : heroList) {
                bw.write(hero.toString());
                bw.newLine();
            }
            System.out.println("排序结果已写入：" + writePath);
        } catch (IOException e) {
            System.err.println("写入文件失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
