package boot.controller;

import boot.model.Enemy;
import boot.model.Player;
import boot.model.User;
import boot.repository.PlayerRepo;
import boot.storage.UserStorage;
import boot.util.Location;
import boot.util.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class GameController {

    @Autowired
    private PlayerRepo playerRepo;
    private Random random;
    private byte steps;
    private byte location = -1;
    private Enemy enemy;
    private boolean session;

    public GameController() {
        this.random = new Random();
    }

    private Enemy createEnemy() {
        return new Enemy(Type.values()[random.nextInt(Type.values().length - 1)], (byte) (random.nextInt(getCurrentPlayer().getLevel() + 3) - 1));
    }

    private String gameOver(boolean isWin) {
        return isWin ? "You Win! Your profile was saved.\n" : "You Lose! Your profile was not saved.\n";
    }

    private void createPlayer() {
        User user = UserStorage.getUser();
        Player player = new Player(user.getLogin());
        playerRepo.save(player);

        System.out.println(user);
        System.out.println("created player for user " + user.getLogin());
    }

    @RequestMapping(value = "/play")
    private String play() {
        enemy = null;
        if (playerRepo == null || getCurrentPlayer() == null) {
            createPlayer();
        }
        session = true;
        location = 0;
        steps = 1;
        if (random.nextInt(100) < Location.values()[location].encounterChance) {
            enemy = createEnemy();
        }
        return printStats();
    }

    @RequestMapping(value = "/attack")
    private String attack() {
        StringBuilder result = new StringBuilder();
        if (session && enemy != null) {
            Player player = getCurrentPlayer();
            if (player.getSpeed() > enemy.getSpeed()) {
                if (random.nextInt(100) < player.getAccuracy() || random.nextInt(100) < player.getLuck()) {
                    enemy.getDamage(player.dealDamage());
                    result.append("You attacked ");
                    result.append(enemy.getType());
                    result.append(" and dealt ");
                    result.append(player.dealDamage());
                    result.append(" damage.\n");
                    System.out.println(enemy.toString());
                } else result.append("You missed!\n");
            }
            while (!enemy.isDead() || !player.isDead()) {
                if (random.nextInt(100) < enemy.getAccuracy() || random.nextInt(100) < enemy.getLuck()) {
                    player.getDamage(enemy.dealDamage());
                    result.append(enemy.getType());
                    result.append(" attacked you");
                    result.append(" and dealt ");
                    result.append(player.dealDamage());
                    result.append(" damage.\n");
                    System.out.println(player.toString());
                } else {
                    result.append(enemy.getType());
                    result.append(" is missed!\n");
                }
                if (random.nextInt(100) < player.getAccuracy() || random.nextInt(100) < player.getLuck()) {
                    enemy.getDamage(player.dealDamage());
                    result.append("You attacked ");
                    result.append(enemy.getType());
                    result.append(" and dealt ");
                    result.append(player.dealDamage());
                    result.append(" damage.\n");
                    System.out.println(enemy.toString());
                } else result.append("You missed!\n");
            }
            if (enemy.isDead()) {
                nextStep(player, result);
                if (random.nextInt(100) < Location.values()[location].encounterChance) {
                    enemy = createEnemy();
                }
            }
            if (player.isDead()) {
                result.append("You are dead!\n");
                result.append(gameOver(false));
                session = false;
                return result.toString();
            }
        } else play();
        result.append(printStats());
        return result.toString();
    }

    @RequestMapping(value = "/escape")
    private String escape() {
        StringBuilder result = new StringBuilder();
        Player player = getCurrentPlayer();
        if (session) {
            if (enemy != null) {
                if (random.nextInt(100) < Location.values()[location].evadeChance || random.nextInt(100) < player.getLuck()) {
                    result.append("You got through the ");
                    result.append(enemy.getType());
                    result.append("\n");
                    nextStep(player, result);
                    enemy = null;
                    if (random.nextInt(100) < Location.values()[location].encounterChance) {
                        enemy = createEnemy();
                    }
                } else {
                    result.append("You couldn`t get through the ");
                    result.append(enemy.getType());
                    result.append("\n");
                    result.append(attack());
                }
            } else {
                nextStep(player, result);
                if (random.nextInt(100) < Location.values()[location].encounterChance) {
                    enemy = createEnemy();
                }
            }
        } else play();
        result.append(printStats());
        return result.toString();
    }

    private void nextStep(Player player, StringBuilder result) {
        if (steps >= Location.values()[location].steps) {
            if (location >= Location.values().length - 1) {
                playerRepo.save(player);
                result.append(gameOver(true));
                session = false;
            } else {
                location++;
                steps = 1;
            }
        } else {
            steps++;
        }
    }

    private String printStats() {
        Player player = getCurrentPlayer();
        StringBuilder result = new StringBuilder();
        if (player != null) {
            result.append(player.toString());
            result.append("\n\n");
        }
        if (session) {
            if (enemy != null) {
                result.append(enemy.toString());
                result.append("\n\n");
            }
            if (location != -1 || location >= Location.values().length) {
                result.append(Location.values()[location]);
                result.append("\nStep: ");
                result.append(steps);
                result.append("/");
                result.append(Location.values()[location].steps);
            }
        }
        return result.toString();
    }

    private Player getCurrentPlayer() {
        return playerRepo.findByName(UserStorage.getUser().getLogin());
    }
}
