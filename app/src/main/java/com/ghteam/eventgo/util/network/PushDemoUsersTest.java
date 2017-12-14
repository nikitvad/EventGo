package com.ghteam.eventgo.util.network;

import android.util.Log;

import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.data.entity.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by nikit on 08.12.2017.
 */

public class PushDemoUsersTest {

    private ArrayList<String> firstNames;
    private ArrayList<String> lastNames;
    private ArrayList<String> images;
    private ArrayList<Category> categories;

    private Random random;

    public PushDemoUsersTest() {

        random = new Random(1375);

        String[] stringArr = {"Антон", "Максим", "Богдан", "Геннадий", "Николай",
                "Гавриил", "Богдан", "Олег", "Святослав", "Иван", "Борис"};
        firstNames = new ArrayList<>(Arrays.asList(stringArr));

        stringArr = new String[]{"Казаков", "Ковалёв", "Белозёров", "Блинов", "Щербаков", "Миронов",
                "Молчанов", "Миронов", "Петров", "Гусев", "Беспалов"};

        lastNames = new ArrayList<>(Arrays.asList(stringArr));

        stringArr = new String[]{"https://www.google.com.ua/imgres?imgurl=http%3A%2F%2Fwww.qygjxz.com%2Fdata%2Fout%2F190%2F6179593-profile-pics.jpg&imgrefurl=http%3A%2F%2Fwww.qygjxz.com%2Fprofile-pics.html&docid=YAsczUmHASI1YM&tbnid=TYfJzUB_6JYtoM%3A&vet=10ahUKEwit_pDIn_rXAhXLChoKHYfpDsQQMwg9KAIwAg..i&w=500&h=500&hl=ru&bih=584&biw=619&q=profile%20picture&ved=0ahUKEwit_pDIn_rXAhXLChoKHYfpDsQQMwg9KAIwAg&iact=mrc&uact=8",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS6ttq8jXcIvxmp2luow2Aa0eQYG8vO3CMKf1zX2Y6JQChsH-oh",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJNtdspFBnm_n6ugwDkCJ_nqiRlnxhryy9s7lA5eJ4ztKVpJiF",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRioTs82DTVe09GbAVBTDOANkXfdbdtkFMODvElg_fyzW7Zt7w3",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSGVNbw0W_N37-ypxWEPs2Sxiag3JQZqZ1FlxR8w2gv4GcKYZlf",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8waOyhG4Jvx2srx6y8-ctmhie8vwCf-BXV575ZkpW_ryVn_bUDQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSI0e_XDplnyZMllLVN0LOYd3EvunlkozRo2zA9brg9-8VC-5DoUg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRBIrgaYucvaAPRc5rX4h3sPQFDlH-fYFpGqpon_IhbkubfcuBZ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR71ru-7LG2-ruk8wXT30UmzYp4FbV7zXE7vogSttCZwfcZIH_G",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcShesBGDYx9hCnhcw0UxU2kiwoM8V7tKNfoZAMqmVkwElI9gqu9Tg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTGgvYQev1gErav2Cd0yaHJ0jgLMlpd1L0Zdfq5ujTSWBFpCEw5Tg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSWz-db9BlSeCFXUO5iEzIPq-rQb6xvln6o5NWJ6vbuqYlk5fXjaw"};

        images = new ArrayList<>(Arrays.asList(stringArr));

        categories = new ArrayList<>();

        categories.add(new Category("trips", "https://cdn2.iconfinder.com/data/icons/maps-navigation-glyph-black/614/3722_-_Route_I-512.png"));
        categories.add(new Category("walking", "https://d30y9cdsu7xlg0.cloudfront.net/png/19727-200.png"));
        categories.add(new Category("coffee", "http://cdn.mysitemyway.com/etc-mysitemyway/icons/legacy-previews/icons/glossy-black-icons-food-beverage/056880-glossy-black-icon-food-beverage-coffee-tea.png"));

    }

    public void pushUsers() {
        List<User> users = generateUsers();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        for (User item : users) {
            DocumentReference documentReference = firestore.collection("users").document();

            Log.d("ssfsf", "pushUsers: " + documentReference.getId());
            item.setId(documentReference.getId());
            documentReference.set(item);
        }

    }

    public List<User> generateUsers() {
        List<User> users = new ArrayList<>();
        for (String firstName : firstNames) {

            for (String lastName : lastNames) {

                String image = images.get(random.nextInt(images.size() - 1));
                User user = new User(firstName, lastName);
                user.setProfileImageUrl(image);
                user.setInterests(categories);
                users.add(user);
            }
        }
        return users;
    }
}
