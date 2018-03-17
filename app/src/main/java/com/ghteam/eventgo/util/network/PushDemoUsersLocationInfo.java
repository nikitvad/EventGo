package com.ghteam.eventgo.util.network;

import com.ghteam.eventgo.data.entity.AppLocation;
import com.ghteam.eventgo.data.entity.Category;
import com.ghteam.eventgo.data.entity.UserLocationInfo;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by nikit on 14.03.2018.
 */

public class PushDemoUsersLocationInfo {
    /*
      CollectionReference collectionReference = FirestoreUtil.getReferenceToUserLocationInfo();

            UserLocationInfo userLocationInfo = new UserLocationInfo();

            com.ghteam.eventgo.data.entity.AppLocation userLocation = new com.ghteam.eventgo.data.entity.AppLocation();

            userLocation.setLatitude(location.getLatitude());
            userLocation.setLongitude(location.getLongitude());

            Log.d("sadfasdfasdf", "onLocationChanged: " + location.toString());

            userLocationInfo.setAppLocation(userLocation);
            userLocationInfo.setUserDisplayName(PrefsUtil.getUserDisplayName());
            userLocationInfo.setUserId(PrefsUtil.getUserId());
            userLocationInfo.setDate(new Date());

            collectionReference.document(PrefsUtil.getUserId()).set(userLocationInfo.toMap());
     */
    private ArrayList<String> firstNames;
    private ArrayList<String> lastNames;
    private ArrayList<String> images;
    private ArrayList<Category> categories;
    private AppLocation[] appLocations;

    private Random random;

    public PushDemoUsersLocationInfo() {
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

        appLocations = new AppLocation[]{new AppLocation(49.425550, 32.063306),
                new AppLocation(49.442383, 32.021542),
                new AppLocation(49.423209, 32.038296),
                new AppLocation(49.417605, 32.097181),
                new AppLocation(49.451083, 32.063860),
                new AppLocation(49.542057, 31.874852),
                new AppLocation(49.234138, 31.872325),
                new AppLocation(49.039729, 32.103004),
                new AppLocation(50.426008, 30.502608),
                new AppLocation(50.492599, 30.449120),
                new AppLocation(50.508476, 30.607208),
                new AppLocation(49.816316, 30.118760)};

    }

    public void push(){
        CollectionReference collectionReference = FirestoreUtil.getReferenceToUserLocationInfo();

        for(UserLocationInfo item: generateUsersInfoLocation()){
            collectionReference.document().set(item.toMap());
        }

    }

    private List<UserLocationInfo> generateUsersInfoLocation() {
        List<UserLocationInfo> result = new ArrayList<>();

        for (int i = 0; i < firstNames.size(); i++) {



            for (int j = 0; j < lastNames.size(); j++) {
                String fullName = firstNames.get(random.nextInt(firstNames.size())) + " " + lastNames.get(random.nextInt(lastNames.size()));

                for (int k = 0; k < appLocations.length; k++) {
                    UserLocationInfo userLocationInfo = new UserLocationInfo();
                    userLocationInfo.setUserDisplayName(fullName);
                    userLocationInfo.setAppLocation(appLocations[random.nextInt(appLocations.length)]);
                    userLocationInfo.setDate(new Date());
                    userLocationInfo.setProfileImageUrl(images.get(random.nextInt(images.size())));
                    userLocationInfo.setUserId("FSDHTHTY5223RGHE4BE");

                    result.add(userLocationInfo);
                }

            }
        }
        return result;
    }
}
