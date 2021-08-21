import java.util.Random;

public abstract class BattleLoc extends Location {
    protected Obstacle obstacle;
    protected String award;

    BattleLoc(Player player, String name, Obstacle obstacle, String award) {
        super(player);
        this.obstacle = obstacle;
        this.name = name;
        this.award = award;
    }

    public boolean getLocation() {
        int obsCount = obstacle.count();
        System.out.println("�uan buradas�n�z : " + this.getName());
        System.out.println("Dikkatli ol! Burada " + obsCount + " tane " + obstacle.getName() + " ya��yor !");
        System.out.print("<S>ava� veya <K>a� :");
        String selCase = scan.nextLine();
        selCase = selCase.toUpperCase();
        if (selCase.equals("S")) {
            if (combat(obsCount)) {
                System.out.println(this.getName() + " b�lgesindeki t�m d��manlar� temizlediniz !");
                if (this.award.equals("Food") && !player.getInv().isFood()) {
                    System.out.println(this.award + " Kazand�n�z! ");
                    player.getInv().setFood(true);
                } else if (this.award.equals("Water") && !player.getInv().isWater()) {
                    System.out.println(this.award + " Kazand�n�z! ");
                    player.getInv().setWater(true);
                } else if (this.award.equals("Firewood") && !player.getInv().isFirewood()) {
                    System.out.println(this.award + " Kazand�n�z! ");
                    player.getInv().setFirewood(true);
                } else if (this.award.equals("Item")) {
                    getRewards();

                }
                return true;
            }

            if (player.getHealthy() <= 0) {
                System.out.println("�ld�n�z !");
                return false;
            }

        }
        return true;
    }

    protected void getRewards() {
        Random random = new Random();
        int chance = random.nextInt(99);
        if (chance < 45) {
            System.out.println("�ans�n�z yokmu� hi�bir�ey kazanamad�n�z");
        } else if (chance < 70) {
            System.out.println("Tebrikler Para Kazand�n�z.");
            int goldChance = random.nextInt(9);
            if (goldChance < 5) {
                player.setMoney(player.getMoney() + 1);
                System.out.println("G�ncel Paran�z : " + player.getMoney());
            } else if (goldChance < 7) {
                player.setMoney(player.getMoney() + 3);
                System.out.println("G�ncel Paran�z : " + player.getMoney());
            } else if (goldChance < 9) {
                player.setMoney(player.getMoney() + 1);
                System.out.println("G�ncel Paran�z : " + player.getMoney());
            }
        } else if (chance < 85) {
            int armorChance = random.nextInt(9);
            if (armorChance < 5) {
                System.out.println("Tebrikler Hafif Z�rh Kazand�n�z !");
                player.getInv().setaName("Hafif Z�rh");
                player.getInv().setArmor(1);
            } else if (armorChance < 7) {
                System.out.println("Tebrikler Orta Z�rh Kazand�n�z !");
                player.getInv().setaName("Orta Z�rh");
                player.getInv().setArmor(3);
            } else if (armorChance < 9) {
                System.out.println("Tebrikler A��r Z�rh Kazand�n�z !");
                player.getInv().setaName("A��r Z�rh");
                player.getInv().setArmor(7);
            }
        } else {
            int weaponChance = random.nextInt(9);
            if (weaponChance < 5) {
                System.out.println("Tebrikler TabancaKazand�n�z !");
                player.getInv().setwName("Tabanca");
                player.getInv().setDamage(2);
            } else if (weaponChance < 7) {
                System.out.println("Tebrikler K�l�� Kazand�n�z !");
                player.getInv().setwName("K�l��");
                player.getInv().setDamage(3);
            } else if (weaponChance < 9) {
                System.out.println("Tebrikler T�fek Kazand�n�z !");
                player.getInv().setwName("T�fek");
                player.getInv().setDamage(7);
            }
        }
    }

    public boolean combat(int obsCount) {
        Random rand = new Random();
        for (int i = 0; i < obsCount; i++) {
            int defObsHealth = obstacle.getHealth();
            playerStats();
            enemyStats();
            while (player.getHealthy() > 0 && obstacle.getHealth() > 0) {
                System.out.print("<V>ur veya <K>a� :");
                String selCase = scan.nextLine();
                selCase = selCase.toUpperCase();
                if (selCase.equals("V")) {
                    int chance = rand.nextInt() % 2;
                    if (chance == 0) {
                        System.out.println("Siz vurdunuz !");
                        obstacle.setHealth(obstacle.getHealth() - player.getTotalDamage());
                        afterHit();
                        if (obstacle.getHealth() > 0) {
                            System.out.println();
                            System.out.println("Canavar size vurdu !");
                            player.setHealthy(player.getHealthy() - (obstacle.getDamage() - player.getInv().getArmor()));
                            afterHit();
                        }
                    } else {
                        System.out.println("Canavar size vurdu !");
                        player.setHealthy(player.getHealthy() - (obstacle.getDamage() - player.getInv().getArmor()));
                        afterHit();
                        if (player.getHealthy() > 0) {
                            System.out.println();
                            System.out.println("Siz vurdunuz !");
                            obstacle.setHealth(obstacle.getHealth() - player.getTotalDamage());
                            afterHit();

                        }
                    }

                } else {
                    return false;
                }
            }

            if (obstacle.getHealth() < player.getHealthy()) {
                System.out.println("D��man� yendiniz !");
                player.setMoney(player.getMoney() + obstacle.getAward());
                System.out.println("G�ncel Paran�z : " + player.getMoney());
                obstacle.setHealth(defObsHealth);
            } else {
                return false;
            }
            System.out.println("-------------------");
        }
        return true;
    }

    public void playerStats() {
        System.out.println("Oyuncu De�erleri\n--------------");
        System.out.println("Can:" + player.getHealthy());
        System.out.println("Hasar:" + player.getTotalDamage());
        System.out.println("Para:" + player.getMoney());
        if (player.getInv().getDamage() > 0) {
            System.out.println("Silah:" + player.getInv().getwName());
        }
        if (player.getInv().getArmor() > 0) {
            System.out.println("Z�rh:" + player.getInv().getaName());
        }
    }

    public void enemyStats() {
        System.out.println("\n" + obstacle.getName() + " De�erleri\n--------------");
        System.out.println("Can:" + obstacle.getHealth());
        System.out.println("Hasar:" + obstacle.getDamage());
        System.out.println("�d�l:" + obstacle.getAward());
    }

    public void afterHit() {
        System.out.println("Oyuncu Can�:" + player.getHealthy());
        System.out.println(obstacle.getName() + " Can�:" + obstacle.getHealth());
        System.out.println();
    }

}
