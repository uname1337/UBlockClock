package com.uname1337.uclock;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ClockRenderer {

    private static final Map<Character, boolean[][]> DIGITS = new HashMap<>();
    private static final Material MATERIAL = Material.WHITE_CONCRETE;

    static {
        DIGITS.put('0', new boolean[][] {
                {true, true, true, true, true},
                {true, false, false, false, true},
                {true, false, false, false, true},
                {true, false, false, false, true},
                {true, false, false, false, true},
                {true, false, false, false, true},
                {true, true, true, true, true}
        });
        DIGITS.put('1', new boolean[][] {
                {false, false, true, false, false},
                {false, true, true, false, false},
                {false, false, true, false, false},
                {false, false, true, false, false},
                {false, false, true, false, false},
                {false, false, true, false, false},
                {false, true, true, true, false}
        });
        DIGITS.put('2', new boolean[][] {
                {true, true, true, true, true},
                {false, false, false, false, true},
                {false, false, false, false, true},
                {false, false, true, true, true},
                {false, true, false, false, false},
                {true, false, false, false, false},
                {true, true, true, true, true}
        });
        DIGITS.put('3', new boolean[][] {
                {true, true, true, true, true},
                {false, false, false, false, true},
                {false, false, false, false, true},
                {false, false, true, true, true},
                {false, false, false, false, true},
                {false, false, false, false, true},
                {true, true, true, true, true}
        });
        DIGITS.put('4', new boolean[][] {
                {false, false, false, true, false},
                {false, false, true, true, false},
                {false, true, false, true, false},
                {true, false, false, true, false},
                {true, true, true, true, true},
                {false, false, false, true, false},
                {false, false, false, true, false}
        });
        DIGITS.put('5', new boolean[][] {
                {true, true, true, true, true},
                {true, false, false, false, false},
                {true, false, false, false, false},
                {true, true, true, true, true},
                {false, false, false, false, true},
                {false, false, false, false, true},
                {true, true, true, true, true}
        });
        DIGITS.put('6', new boolean[][] {
                {true, true, true, true, true},
                {true, false, false, false, false},
                {true, false, false, false, false},
                {true, true, true, true, true},
                {true, false, false, false, true},
                {true, false, false, false, true},
                {true, true, true, true, true}
        });
        DIGITS.put('7', new boolean[][] {
                {true, true, true, true, true},
                {false, false, false, false, true},
                {false, false, false, true, false},
                {false, false, true, false, false},
                {false, true, false, false, false},
                {false, true, false, false, false},
                {false, true, false, false, false}
        });
        DIGITS.put('8', new boolean[][] {
                {true, true, true, true, true},
                {true, false, false, false, true},
                {true, false, false, false, true},
                {true, true, true, true, true},
                {true, false, false, false, true},
                {true, false, false, false, true},
                {true, true, true, true, true}
        });
        DIGITS.put('9', new boolean[][] {
                {true, true, true, true, true},
                {true, false, false, false, true},
                {true, false, false, false, true},
                {true, true, true, true, true},
                {false, false, false, false, true},
                {false, false, false, false, true},
                {true, true, true, true, true}
        });
        DIGITS.put(':', new boolean[][] {
                {false, false, false, false, false},
                {false, false, true, false, false},
                {false, false, true, false, false},
                {false, false, false, false, false},
                {false, false, true, false, false},
                {false, false, true, false, false},
                {false, false, false, false, false}
        });
    }

    public void renderAll() {
        for (ClockRegion clock : UClockPlugin.getInstance().getClocks().values()) {
            renderClock(clock);
        }
    }

    public void renderClock(ClockRegion clock) {
        World world = Bukkit.getWorld(clock.worldName());
        if (world == null) {
            return;
        }

        int minX = Math.min(clock.minX(), clock.maxX());
        int maxX = Math.max(clock.minX(), clock.maxX());
        int minY = Math.min(clock.minY(), clock.maxY());
        int maxY = Math.max(clock.minY(), clock.maxY());
        int minZ = Math.min(clock.minZ(), clock.maxZ());
        int maxZ = Math.max(clock.minZ(), clock.maxZ());

        clearRegion(world, minX, minY, minZ, maxX, maxY, maxZ);

        String timeText = ZonedDateTime.now(ZoneId.of("Europe/Moscow")).format(DateTimeFormatter.ofPattern("HH:mm"));

        int digitWidth = 5;
        int digitHeight = 7;
        int gap = 1;
        int totalWidth = (timeText.length() * digitWidth) + ((timeText.length() - 1) * gap);

        if (totalWidth > (maxX - minX + 1) || digitHeight > (maxY - minY + 1)) {
            return;
        }

        int cursorX = minX;
        int cursorY = minY;
        for (int i = 0; i < timeText.length(); i++) {
            boolean[][] pattern = DIGITS.get(timeText.charAt(i));
            if (pattern == null) {
                continue;
            }
            for (int row = 0; row < digitHeight; row++) {
                for (int col = 0; col < digitWidth; col++) {
                    if (pattern[row][col]) {
                        setBlock(world, cursorX + col, cursorY + row, minZ);
                    }
                }
            }
            cursorX += digitWidth + gap;
        }
    }

    private void clearRegion(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() != Material.AIR) {
                        block.setType(Material.AIR, false);
                    }
                }
            }
        }
    }

    private void setBlock(World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        if (block.getType() != MATERIAL) {
            block.setType(MATERIAL, false);
        }
    }
}
