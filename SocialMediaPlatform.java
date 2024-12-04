import java.util.*;

public class SocialMediaPlatform {
    public static void main(String[] args) {

        User asya = new User("Asya");
        User alisa = new User("Alisa");
        User vera = new User("Vera");
        System.out.println();

        vera.follow(asya);
        asya.follow(alisa);
        alisa.follow(vera);
        System.out.println();

        alisa.createPost("Java ile Her Gün Daha İyi Kod Yaz \uD83D\uDE80");
        vera.createPost("Kodlamada Sürekli Gelişim Önemlidir \uD83D\uDCA1!");
        asya.createPost("Java'da Lambda Fonksiyonlarına Giriş! ⚡");
        System.out.println();

        asya.addCommentToPost(vera, 1, "Her gün küçük ipuçlarıyla büyük farklar yaratıyoruz.");
        vera.addCommentToPost(alisa, 0, "Her gün yeni bir şey öğrenmenin heyecanı.");
        alisa.addCommentToPost(asya, 2, "Lambda ifadeleri sayesinde kod daha kısa ve daha temiz.");
        System.out.println();

        alisa.addToPostFavorites(asya, 1);
        vera.addToPostFavorites(alisa, 2);


        System.out.println("\n--- Alisa'nın Ana Sayfası ---");
        alisa.showFeed();

        System.out.println("\n--- Asya'nın Ana Sayfası ---");
        asya.showFeed();

        System.out.println("\n--- Vera'nın Ana Sayfası ---");
        vera.showFeed();


        System.out.println("\n--- Alisa'nın Gönderileri ---");
        alisa.showPosts();

        System.out.println("\n--- Asya'nın Gönderileri ---");
        asya.showPosts();

        System.out.println("\n--- Vera'nın Gönderileri ---");
        vera.showPosts();

        System.out.println("\n--- Alisa'nın Favorileri ---");
        alisa.showFavorites();

        System.out.println("\n--- Asya'nın  Favorileri ---");
        asya.showFavorites();

        System.out.println("\n--- Vera'nın Favorileri ---");
        vera.showFavorites();

    }

    static class User{
        private String name;
        private LinkedHashMap<Integer, Post> posts; // Kullanıcının gönderileri
        private HashSet<User> following; // Kullanıcının takip ettiği kişiler
        private TreeSet<Post> favorites; // Kullanıcının beğendiği gönderiler
        private ArrayList<Post> feed; // Kullanıcının takip ettiği kişilerin gönderileri
        private HashMap<Integer, Comment> allComments; // Tüm yorumlar için bir HashMap
        private static int postCounter = 0; // Gönderi sayacı

        public User (String name){
            this.name = name;
            this.posts = new LinkedHashMap<>();
            this.following = new HashSet<>();
            this.favorites = new TreeSet<>();
            this.feed = new ArrayList<>();
            this.allComments = new HashMap<>();
        }

        public void follow(User user){
            following.add(user);
            System.out.println(name + ", " + user.getName() + " kullanıcısını takip ediyor.");
        }

        public void createPost (String content){
            Post newPost = new Post(postCounter++, this, content);
            posts.put(newPost.getId(), newPost);
            System.out.println(name + ", yeni bir gönderi oluşturdu: " + content);
            for (User follower : following) {
                follower.addToFeed(newPost);
            }
        }

        public void addCommentToPost (User user, int postId, String comment){
            Post post = user.getPost(postId);
            if (post != null){
                Comment newComment = new Comment(this, comment);
                post.addComment(newComment);
                allComments.put(allComments.size(), newComment);
                System.out.println(name + ", " + user.getName() + " kullanıcısının gönderisine yorum yaptı: " + comment);
            }
        }

        public void addToPostFavorites (User user, int postId){
            Post post = user.getPost(postId);
            if (post != null){
                favorites.add(post);
                System.out.println(name + ", " + user.getName() + " kullanıcısının gönderisini beğendi: " + post.getContent());
            }
        }

        public void showFeed(){
            System.out.println("\n" + name + " kullanıcısının Ana Sayfası");
            Iterator<Post> iterator = feed.iterator();
            while (iterator.hasNext()) {
                Post post = iterator.next();
                System.out.println(name + " kullanıcısının takip ettiği bir gönderi: " + post.getContent());
            }
        }

        public void addToFeed(Post post) {
            feed.add(post);
        }

        public Post getPost(int postId){
            return posts.get(postId);
        }

        public void showPosts(){
            for(Post post : posts.values()){
                System.out.println(name + " kullanıcısının gönderisi: " + post.getContent());
                post.showComments();
            }
        }

        public void showFavorites(){
            System.out.println(name + " kullanıcısının favori gönderileri: ");
            if (favorites.isEmpty()){
                System.out.println("Henüz favori gönderi yok.");
            }else {
                Iterator<Post> iterator = favorites.iterator();
                while (iterator.hasNext()) {
                    Post post = iterator.next();
                    System.out.println("Favori gönderi: " + post.getContent());
                }
            }
        }

        public class Post implements Comparable<Post> {
            private int id;
            private User user;
            private String content;
            private LinkedList<Comment> comments;

            public Post(int id, User user, String content){
                this.id = id;
                this.user = user;
                this.content = content;
                this.comments = new LinkedList<>();
            }

            public int getId(){
                return id;
            }

            public String getContent(){
                return content;
            }

            public void addComment(Comment comment){
                comments.add(comment);
            }

            public void showComments(){
                System.out.println("Gönderiye yapılan yorumlar: ");
                Iterator<Comment> iterator = comments.iterator();
                while (iterator.hasNext()) {
                    Comment comment = iterator.next();
                    System.out.println(comment.getContent());
                }
            }

            @Override
            public int compareTo(Post other){
                return Integer.compare(this.id, other.id);
            }
        }

        public class Comment{
            private User user;
            private  String content;

            public Comment(User user, String content){
                this.user = user;
                this.content = content;
            }
            public String getContent(){
                return content;
            }
        }
        public String getName(){
            return name;
        }
    }
}