/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laytonsmith.abstraction.bukkit;

import com.laytonsmith.abstraction.*;
import com.laytonsmith.abstraction.blocks.MCBlock;
import com.laytonsmith.abstraction.bukkit.blocks.BukkitMCBlock;
import com.laytonsmith.abstraction.bukkit.events.BukkitEntityEvents;
import com.laytonsmith.abstraction.events.MCEntityDamageEvent;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CNull;
import com.laytonsmith.core.constructs.Construct;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.MobEffect;
import net.minecraft.server.ServerConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

/**
 *
 * @author layton
 */
public class BukkitMCPlayer extends BukkitMCHumanEntity implements MCPlayer, MCCommandSender {

    Player p;

    public BukkitMCPlayer(Player p) {
        super(p);
        this.p = p;
    }

    public Player _Player(){
        return p;
    }

    public void addEffect(int potionID, int strength, int seconds) {        
        EntityPlayer ep = ((CraftPlayer) p).getHandle();        
        MobEffect me = new MobEffect(potionID, seconds * 20, strength);
        //ep.addEffect(me);
        //ep.b(me);
        
        Class epc = EntityLiving.class;
        try {
            Method meth = epc.getDeclaredMethod("b", net.minecraft.server.MobEffect.class);
            //ep.d(new MobEffect(effect, seconds * 20, strength));
            //Call it reflectively, because it's deobfuscated in newer versions of CB
            meth.invoke(ep, me);
        } catch (Exception e) {
            try {
                //Look for the addEffect version                
                Method meth = epc.getDeclaredMethod("addEffect", MobEffect.class);
                //ep.addEffect(me);
                meth.invoke(ep, me);
            } catch (Exception ex) {
                Logger.getLogger(BukkitMCPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean canSee(MCPlayer p) {
        return this.p.canSee(((BukkitMCPlayer)p)._Player());
    }

    public void chat(String chat) {
        p.chat(chat);
    }

    public InetSocketAddress getAddress(){
        return p.getAddress();
    }

    public boolean getAllowFlight(){
        return p.getAllowFlight();
    }

    public MCLocation getCompassTarget() {
        return new BukkitMCLocation(p.getCompassTarget());
    }

    public String getDisplayName() {
        return p.getDisplayName();
    }

    public int getEntityId() {
        return p.getEntityId();
    }

    public float getExp() {
        return p.getExp();
    }

    public int getFoodLevel() {
        return p.getFoodLevel();
    }

    public MCGameMode getGameMode() {
        switch(p.getGameMode()){
            case SURVIVAL:
                return MCGameMode.SURVIVAL;
            case CREATIVE:
                return MCGameMode.CREATIVE;
        }
        return null;
    }
    
    public int getHealth() {
        return p.getHealth();
    }
    
    
    public MCInventory getInventory() {
        if (p == null || p.getInventory() == null) {
            return null;
        }
        return new BukkitMCInventory(p.getInventory());
    }
    
    public MCItemStack getItemAt(Construct construct) {
        if(construct == null || construct instanceof CNull){
            return new BukkitMCItemStack(p.getItemInHand());
        }
        int slot = (int) Static.getInt(construct);
        ItemStack is = null;
        //Special slots
        if(slot == 100){
            is = p.getInventory().getBoots();
        } else if(slot == 101){
            is = p.getInventory().getLeggings();
        } else if(slot == 102){
            is = p.getInventory().getChestplate();
        } else if(slot == 103){
            is = p.getInventory().getHelmet();
        }
        if(slot >= 0 && slot <= 35){
            is = p.getInventory().getItem(slot);
        }
        if(is == null){
            return null;
        } else {
            return new BukkitMCItemStack(is);
        }
    }
    
    public MCItemStack getItemInHand() {
        if (p == null || p.getItemInHand() == null) {
            return null;
        }
        return new BukkitMCItemStack(p.getItemInHand());
    }

    public int getLevel() {
        return p.getLevel();
    }

    private List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance, int maxLength) {
        if (maxDistance > 512) {
            maxDistance = 512;
        }
        ArrayList<Block> blocks = new ArrayList<Block>();
        Iterator<Block> itr = new BlockIterator(p, maxDistance);
        while (itr.hasNext()) {
            Block block = itr.next();
            blocks.add(block);
            if (maxLength != 0 && blocks.size() > maxLength) {
                blocks.remove(0);
            }
            int id = block.getTypeId();
            if (transparent == null) {
                if (id != 0) {
                    break;
                }
            } else {
                if (!transparent.contains((byte) id)) {
                    break;
                }
            }
        }
        return blocks;
    }
    
    public MCPlayer getPlayer() {
        return new BukkitMCPlayer(p);
    }

    public long getPlayerTime() {
        return p.getPlayerTime();
    }

    public int getRemainingFireTicks() {
        return p.getFireTicks();
    }

    public MCBlock getTargetBlock(HashSet<Byte> b, int i){
        return new BukkitMCBlock(getFirstTargetBlock(b, i));
    }

    private Block getFirstTargetBlock(HashSet<Byte> transparent, int maxDistance) {
        List<Block> blocks = getLineOfSight(transparent, maxDistance, 1);
        return blocks.get(0);
    }

    public int getTotalExperience() {
        return p.getTotalExperience();
    }

    public void giveExp(int xp) {
        p.giveExp(xp);
    }

    public boolean isBanned() {
        return p.isBanned();
    }

    public boolean isOnline() {
        return p.isOnline();
    }

    public boolean isOp() {
        return p.isOp();
    }

    public boolean isSneaking() {
        return p.isSneaking();
    }

    public boolean isWhitelisted() {
        return p.isWhitelisted();
    }

    public void kickPlayer(String message) {
        p.kickPlayer(message);
    }

    public boolean removeEffect(int potionID){
//        p.removePotionEffect(PotionEffectType.getById(potionID));
        EntityPlayer ep = ((CraftPlayer) p).getHandle();
        try {
            Field f = EntityLiving.class.getDeclaredField("effects");
            f.setAccessible(true);
            HashMap effects = (HashMap)f.get(ep);
            MobEffect me = (MobEffect) effects.get(potionID);
            if(me == null){
                //This mob effect isn't added to this player
                return false;
            }
            Field fDuration = me.getClass().getDeclaredField("duration");
            fDuration.setAccessible(true);
            fDuration.set(me, 0);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(BukkitMCPlayer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IllegalAccessException ex) {
            Logger.getLogger(BukkitMCPlayer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(BukkitMCPlayer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (SecurityException ex) {
            Logger.getLogger(BukkitMCPlayer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
        //Meh, stupid bukkit.
        //ep.effects.remove(potionID);        
    }

    public void resetPlayerTime() {
        p.resetPlayerTime();
    }

    public void sendMessage(String string) {
        p.sendMessage(string);
    }
    
    public void setAllowFlight(boolean flight){
        p.setAllowFlight(flight);
    }

    public void setBanned(boolean banned) {
        p.setBanned(banned);
    }
    
    public void setCompassTarget(MCLocation l) {
        p.setCompassTarget(((BukkitMCLocation)l).l);
    }

    public void setDisplayName(String name) {
        p.setDisplayName(name);
    }

    public void setExp(float i) {
        p.setExp(i);
    }

    public void setFoodLevel(int f) {
        p.setFoodLevel(f);
    }

    public void setGameMode(MCGameMode mode) {
        switch(mode){
            case SURVIVAL:
                p.setGameMode(GameMode.SURVIVAL);
                break;
            case CREATIVE:
                p.setGameMode(GameMode.CREATIVE);
                break;
        }
    }
    
    public void setHealth(int i) { 
        if(i == 0){
            this.fireEntityDamageEvent(MCDamageCause.CUSTOM);
        }
        p.setHealth(i);
    }

    public void setItemInHand(MCItemStack is) {
        p.setItemInHand(((BukkitMCItemStack)is).is);
    }
    
    public void setLevel(int xp) {
        p.setLevel(xp);
    }
    
    public void setPlayerTime(Long time) {
        p.setPlayerTime(time, false);
    }
    
    public void setRemainingFireTicks(int i){
        p.setFireTicks(i);
    }

    public void setTempOp(Boolean value) throws ClassNotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Server server = Bukkit.getServer();

        Class serverClass = Class.forName("org.bukkit.craftbukkit.CraftServer", true, server.getClass().getClassLoader());

        if (!server.getClass().isAssignableFrom(serverClass)) {
            throw new IllegalStateException("Running server isn't CraftBukkit");
        }

        Field opSetField;

        try {
            opSetField = ServerConfigurationManager.class.getDeclaredField("operators");
        } catch (NoSuchFieldException e) {
            opSetField = ServerConfigurationManager.class.getDeclaredField("h");
        }

        opSetField.setAccessible(true); // make field accessible for reflection 

        // Reflection magic
        Set opSet = (Set) opSetField.get((ServerConfigurationManager) serverClass.getMethod("getHandle").invoke(server));

        // since all Java objects pass by reference, we don't need to set field back to object
        if (value) {
            opSet.add(p.getName().toLowerCase());
        } else {
            opSet.remove(p.getName().toLowerCase());
        }
        p.recalculatePermissions();
    }

    public void setTotalExperience(int total) {
        p.setTotalExperience(total);
    }

    public void setVanished(boolean set, MCPlayer to) {
        if(!set){
            p.showPlayer(((BukkitMCPlayer)to)._Player());
        } else {
            p.hidePlayer(((BukkitMCPlayer)to)._Player());
        }
    }

    public void setWhitelisted(boolean value) {
        p.setWhitelisted(value);
    }
}
