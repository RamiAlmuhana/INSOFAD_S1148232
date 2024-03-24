package com.example.todoappdeel3.utils;

import com.example.todoappdeel3.dao.OrderDAO;
import com.example.todoappdeel3.dao.ProductDAO;
import com.example.todoappdeel3.dao.ProductRepository;
import com.example.todoappdeel3.dao.UserRepository;
import com.example.todoappdeel3.models.Category;
import com.example.todoappdeel3.models.CustomUser;
import com.example.todoappdeel3.models.PlacedOrder;
import com.example.todoappdeel3.models.Product;
import org.apache.catalina.User;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class Seeder {
    private ProductDAO productDAO;
    private UserRepository userRepository;
    private OrderDAO orderDAO;
    private ProductRepository productRepository;


    public Seeder(ProductDAO productDAO, UserRepository userRepository, OrderDAO orderDAO, ProductRepository productRepository) {
        this.productDAO = productDAO;
        this.userRepository = userRepository;
        this.orderDAO = orderDAO;
        this.productRepository = productRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event){
        this.seedProducts();
        this.seedUser();
    }

    private void seedProducts(){
        Category FPS = new Category("FPS");
        Category fighter = new Category("Fighter");
        Category soulsLike = new Category("Souls-like");
        Category action = new Category("Action");
        Category creative = new Category("Creative");
        Category openWorld = new Category("Open world");
        Category racing = new Category("Racing");
        Product rainbowSixSiege = new Product("Tom Clancy's Rainbow Six Siege", "Tom Clancy's Rainbow Six® Siege is an elite, tactical team-based shooter where superior planning and execution triumph.", 15.99, "https://store.ubisoft.com/on/demandware.static/-/Sites-masterCatalog/default/dw63e24d90/images/large/56c494ad88a7e300458b4d5a.jpg", FPS);
        Product mortalKombar1 = new Product("Mortal Kombat 1", "Discover a reborn Mortal Kombat™ Universe created by the Fire God Liu Kang. Mortal Kombat™ 1 ushers in a new era of the iconic franchise with a new fighting system, game modes, and fatalities!", 34.99, "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSAMOK3cjH0oK89AraaQ_DJQ3EyioLRJxCnq0X2lLPXDisjyn1XOuwGkRLht5b1c7d7G6Uk2w", fighter);
        Product eldenRing = new Product("Elden Ring", "THE NEW FANTASY ACTION RPG. Rise, Tarnished, and be guided by grace to brandish the power of the Elden Ring and become an Elden Lord in the Lands Between.", 47.99, "https://upload.wikimedia.org/wikipedia/en/b/b9/Elden_Ring_Box_art.jpg", soulsLike);
        Product helldivers2 = new Product("Helldivers 2", "The Galaxy’s Last Line of Offence. Enlist in the Helldivers and join the fight for freedom across a hostile galaxy in a fast, frantic, and ferocious third-person shooter.", 39.99, "https://image.api.playstation.com/vulcan/ap/rnd/202309/0718/60ebb0f1f65149baa3c3ea07b08f8595c17e7759fea79e1c.jpg", action);
        Product codmw3 = new Product("Call of Duty®: Modern Warfare® III", "In the direct sequel to the record-breaking Call of Duty®: Modern Warfare® II, Captain Price and Task Force 141 face off against the ultimate threat.", 69.99, "https://m.media-amazon.com/images/M/MV5BYmNjZDM0M2EtZTFiYy00YjJlLWIzNDUtNjhhMjk2ODNkYzFlXkEyXkFqcGdeQXVyMTcyNjA2NzMx._V1_FMjpg_UX1000_.jpg", FPS);
        Product battlefield = new Product("Battlefield 3", "Enjoy total freedom to fight the way you want. Explore 29 massive multiplayer maps and use loads of vehicles, weapons, and gadgets to help you turn up the heat.", 39.99, "https://upload.wikimedia.org/wikipedia/en/6/69/Battlefield_3_Game_Cover.jpg", FPS);
        Product codBlackOps1 = new Product("Call of Duty®: Black Ops", "The biggest first-person action series of all time and the follow-up to critically acclaimed Call of Duty®: Modern Warfare 2 returns with Call of Duty®: Black Ops.", 39.99, "https://upload.wikimedia.org/wikipedia/en/0/02/CoD_Black_Ops_cover.png", FPS);
        Product minecraft = new Product("Minecraft", "Minecraft is a 2011 sandbox game developed by Mojang Studios and originally released in 2009. The game was created by Markus \"Notch\" Persson in the Java programming language.", 29.99, "https://upload.wikimedia.org/wikipedia/en/5/51/Minecraft_cover.png", creative);
        Product fifa24 = new Product("EA SPORTS FC™ 24", "EA SPORTS FC™ 24 welcomes you to The World’s Game: the most true-to-football experience ever with HyperMotionV, PlayStyles optimised by Opta, and an enhanced Frostbite™ Engine.", 69.99, "https://store-images.s-microsoft.com/image/apps.62211.14281959400456729.871028c8-df4c-403f-826e-d4deb249dc43.b5be9148-05ab-41a0-af06-5a2e7e03d0f3", creative);
        Product borderlands = new Product("Borderlands 3", "The original shooter-looter returns, packing bazillions of guns and a mayhem-fueled adventure! Blast through new worlds and enemies as one of four new Vault Hunters.", 59.99, "https://upload.wikimedia.org/wikipedia/en/2/21/Borderlands_3_cover_art.jpg", creative);
        Product gta6 = new Product("Grand Theft Auto VI", "Grand Theft Auto VI is an upcoming action-adventure game in development by Rockstar Games. It is due to be the eighth main Grand Theft Auto game, following Grand Theft Auto V, and the sixteenth entry overall.", 59.99, "https://upload.wikimedia.org/wikipedia/en/4/46/Grand_Theft_Auto_VI.png", openWorld);
        Product gtaSA = new Product("Grand Theft Auto: San Andreas", "Five years ago Carl Johnson escaped from the pressures of life in Los Santos, San Andreas... a city tearing itself apart with gang trouble, drugs and corruption.", 59.99, "https://upload.wikimedia.org/wikipedia/en/thumb/c/c4/GTASABOX.jpg/220px-GTASABOX.jpg", openWorld);
        Product crashTeamRacing = new Product("Crash Team Racing Nitro-Fueled", "Crash Team Racing Nitro-Fueled is a 2019 kart racing game developed by Beenox and published by Activision.", 29.99, "https://upload.wikimedia.org/wikipedia/en/3/36/Crash_Team_Racing_Nitro-Fueled_cover_art.jpg", racing);
        this.productDAO.createProduct(rainbowSixSiege);
        this.productDAO.createProduct(mortalKombar1);
        this.productDAO.createProduct(eldenRing);
        this.productDAO.createProduct(helldivers2);
        this.productDAO.createProduct(codmw3);
        this.productDAO.createProduct(battlefield);
        this.productDAO.createProduct(codBlackOps1);
        this.productDAO.createProduct(minecraft);
        this.productDAO.createProduct(fifa24);
        this.productDAO.createProduct(borderlands);
        this.productDAO.createProduct(gta6);
        this.productDAO.createProduct(gtaSA);
        this.productDAO.createProduct(crashTeamRacing);
    }

    private void seedUser(){
        CustomUser customUser = new CustomUser();
        customUser.setName("Rami");
        customUser.setInfix("");
        customUser.setLastName("Al-Muhana");
        customUser.setEmail("test@mail.com");
        customUser.setPassword(new BCryptPasswordEncoder().encode("Test123!"));
        userRepository.save(customUser);
    }
}
