package com.ghteam.eventgo.util.network;

import com.ghteam.eventgo.data.model.Category;
import com.ghteam.eventgo.data.model.Event;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by nikit on 09.12.2017.
 */

public class PushDemoEvents {
    private String[] names;
    private String[] descriptions;
    private String[] owners;

    private ArrayList<Category> categories;


    private String[] images;

    private Random r;

    public PushDemoEvents() {

        r = new Random(7365);
        names = new String[10];

        for (int i = 0; i < names.length; i++) {
            names[i] = "event " + i;
        }

        owners = new String[]{"0BRsjAm8McMbKARpX52k",
                "0OW4TtOY5bmScXRTlQ6K",
                "0wklW9DJDXnCzbUrpy2P",
                "1KomcxjBv2RhPmSoFauR",
                "2NNMVTbSRHdq1IfxAPxZ",
                "2fx6yUtOyBTttpTIlht9",
                "3hsSqIMbaCAwLH28hpx1",
                "3wpNv9uv1pLaZBzjPLDe",
                "4LJ13xGIrHkjO5VaoB88",
                "5KqdIxeshsJURp1jxlMc"};

        images = new String[]{"https://www.google.com.ua/imgres?imgurl=http%3A%2F%2Fwww.qygjxz.com%2Fdata%2Fout%2F190%2F6179593-profile-pics.jpg&imgrefurl=http%3A%2F%2Fwww.qygjxz.com%2Fprofile-pics.html&docid=YAsczUmHASI1YM&tbnid=TYfJzUB_6JYtoM%3A&vet=10ahUKEwit_pDIn_rXAhXLChoKHYfpDsQQMwg9KAIwAg..i&w=500&h=500&hl=ru&bih=584&biw=619&q=profile%20picture&ved=0ahUKEwit_pDIn_rXAhXLChoKHYfpDsQQMwg9KAIwAg&iact=mrc&uact=8",
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

        descriptions = new String[]{"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Et netus et malesuada fames ac turpis. Ultrices neque ornare aenean euismod elementum nisi quis. Quis vel eros donec ac odio tempor orci. Molestie at elementum eu facilisis sed odio morbi quis. Curabitur vitae nunc sed velit dignissim sodales ut. Potenti nullam ac tortor vitae purus faucibus ornare. Ac ut consequat semper viverra. Morbi tempus iaculis urna id volutpat. Vel eros donec ac odio tempor orci dapibus ultrices in. Integer enim neque volutpat ac. Sed nisi lacus sed viverra tellus in hac habitasse. Porttitor massa id neque aliquam vestibulum. Cras fermentum odio eu feugiat pretium nibh. Facilisi morbi tempus iaculis urna id volutpat lacus laoreet non. Justo laoreet sit amet cursus sit amet dictum sit. A diam sollicitudin tempor id eu nisl nunc. Interdum velit euismod in pellentesque massa placerat duis ultricies lacus. Varius morbi enim nunc faucibus a pellentesque. Facilisis leo vel fringilla est ullamcorper.",
                "Faucibus a pellentesque sit amet porttitor eget dolor morbi non. Fringilla urna porttitor rhoncus dolor purus non. Arcu ac tortor dignissim convallis aenean. Tortor consequat id porta nibh venenatis cras. Non tellus orci ac auctor augue mauris augue neque gravida. Commodo odio aenean sed adipiscing diam donec adipiscing tristique risus. Lobortis scelerisque fermentum dui faucibus. Nunc lobortis mattis aliquam faucibus purus in massa tempor. Semper eget duis at tellus at urna condimentum mattis. Justo nec ultrices dui sapien eget mi.",
                "Pulvinar mattis nunc sed blandit libero. Cras sed felis eget velit. Lorem ipsum dolor sit amet. Tortor posuere ac ut consequat semper viverra nam libero justo. Cras tincidunt lobortis feugiat vivamus at augue. Sodales ut eu sem integer vitae justo eget magna. Nulla at volutpat diam ut venenatis tellus in metus. Id consectetur purus ut faucibus. Vitae justo eget magna fermentum iaculis eu non diam. Habitasse platea dictumst vestibulum rhoncus. In cursus turpis massa tincidunt dui ut ornare.",
                "Est ultricies integer quis auctor elit sed vulputate mi sit. Non odio euismod lacinia at quis risus sed. Non arcu risus quis varius quam quisque. Pellentesque habitant morbi tristique senectus et netus et malesuada. Lacus viverra vitae congue eu consequat ac felis donec. Tempus iaculis urna id volutpat. Sapien nec sagittis aliquam malesuada bibendum arcu vitae elementum curabitur. Aliquet nec ullamcorper sit amet risus nullam eget. Ut pharetra sit amet aliquam id diam maecenas ultricies. Odio aenean sed adipiscing diam donec adipiscing tristique risus. Neque egestas congue quisque egestas diam in arcu cursus euismod. Nunc faucibus a pellentesque sit amet.",
                "Nullam vehicula ipsum a arcu cursus. Sagittis orci a scelerisque purus semper eget duis. Pellentesque dignissim enim sit amet venenatis urna cursus eget nunc. Vel turpis nunc eget lorem dolor sed viverra ipsum. Ut pharetra sit amet aliquam id diam maecenas ultricies mi. Lectus mauris ultrices eros in cursus turpis. Cras semper auctor neque vitae tempus quam. Mattis rhoncus urna neque viverra justo nec ultrices dui sapien. Elit ullamcorper dignissim cras tincidunt lobortis feugiat vivamus at. Lobortis mattis aliquam faucibus purus in massa tempor nec. Iaculis urna id volutpat lacus. Elit sed vulputate mi sit amet mauris commodo quis imperdiet. At consectetur lorem donec massa sapien faucibus et. In vitae turpis massa sed elementum. Quis varius quam quisque id. Enim ut sem viverra aliquet eget sit. Sed libero enim sed faucibus turpis in eu mi. Facilisi etiam dignissim diam quis enim lobortis scelerisque. Massa vitae tortor condimentum lacinia quis vel eros."
        };


        categories = new ArrayList<>();

        categories.add(new Category("trips", "https://cdn2.iconfinder.com/data/icons/maps-navigation-glyph-black/614/3722_-_Route_I-512.png"));
        categories.add(new Category("walking", "https://d30y9cdsu7xlg0.cloudfront.net/png/19727-200.png"));
        categories.add(new Category("coffee", "http://cdn.mysitemyway.com/etc-mysitemyway/icons/legacy-previews/icons/glossy-black-icons-food-beverage/056880-glossy-black-icon-food-beverage-coffee-tea.png"));

    }

    public void push() {
        List<Event> events = generateDemoEvents();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        for (Event item : events) {
            firestore.collection("events").add(item);
        }
    }


    public List<Event> generateDemoEvents() {
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < names.length - 1; i++) {

            for (String item : descriptions) {

                Event event = new Event();
                event.setName(names[i]);
                event.setDescription(item);
                event.setOwnerId(owners[i]);

                ArrayList<String> eventImages = new ArrayList<>();

                for (int y = 0; y < r.nextInt(images.length - 1); y++) {
                    eventImages.add(images[r.nextInt(images.length - 1)]);
                }
                event.setImages(eventImages);
                event.setCategory(categories.get(r.nextInt(categories.size() - 1)));

                events.add(event);
            }
        }

        return events;
    }
}
