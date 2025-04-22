import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Steganography {

// Activity 1 Methods

/**
* Clear the lower (rightmost) two bits in a pixel.
*/
public static void clearLow(Pixel p) {
p.setRed((p.getRed() / 4) * 4);
p.setGreen((p.getGreen() / 4) * 4);
p.setBlue((p.getBlue() / 4) * 4);
}

/**
* Test clearLow by clearing the low bits of all pixels
*/
public static Picture testClearLow(Picture p) {
Picture newPic = new Picture(p);
Pixel[][] pixels = newPic.getPixels2D();
for (Pixel[] row : pixels) {
for (Pixel pixel : row) {
clearLow(pixel);
}
}
return newPic;
}

/**
* Set the lower 2 bits in a pixel to the highest 2 bits in c
*/
public static void setLow(Pixel p, Color c) {
p.setRed((p.getRed() / 4) * 4 + (c.getRed() / 64));
p.setGreen((p.getGreen() / 4) * 4 + (c.getGreen() / 64));
p.setBlue((p.getBlue() / 4) * 4 + (c.getBlue() / 64));
}

/**
* Test setLow by setting the low bits of all pixels to a color
*/
public static Picture testSetLow(Picture p, Color c) {
Picture newPic = new Picture(p);
Pixel[][] pixels = newPic.getPixels2D();
for (Pixel[] row : pixels) {
for (Pixel pixel : row) {
setLow(pixel, c);
}
}
return newPic;
}

// Activity 2 Methods

/**
* Determines whether secret can be hidden in source
*/
public static boolean canHide(Picture source, Picture secret) {
return source.getWidth() == secret.getWidth() &&
source.getHeight() == secret.getHeight();
}

/**
* Creates a new Picture with data from secret hidden in source
*/
public static Picture hidePicture(Picture source, Picture secret) {
Picture combined = new Picture(source);
Pixel[][] sourcePixels = combined.getPixels2D();
Pixel[][] secretPixels = secret.getPixels2D();

for (int r = 0; r < sourcePixels.length; r++) {
for (int c = 0; c < sourcePixels[0].length; c++) {
Pixel sourcePixel = sourcePixels[r][c];
Pixel secretPixel = secretPixels[r][c];
clearLow(sourcePixel);
setLow(sourcePixel, secretPixel.getColor());
}
}
return combined;
}

/**
* Reveals the hidden picture
*/
public static Picture revealPicture(Picture hidden) {
Picture copy = new Picture(hidden);
Pixel[][] pixels = copy.getPixels2D();

for (int r = 0; r < pixels.length; r++) {
for (int c = 0; c < pixels[0].length; c++) {
Pixel p = pixels[r][c];
p.setRed((p.getRed() % 4) * 64);
p.setGreen((p.getGreen() % 4) * 64);
p.setBlue((p.getBlue() % 4) * 64);
}
}
return copy;
}

// Activity 3 Methods

/**
* Hides a picture at a specific location
*/
public static Picture hidePicture(Picture source, Picture secret,
int startRow, int startCol) {
Picture combined = new Picture(source);
Pixel[][] sourcePixels = combined.getPixels2D();
Pixel[][] secretPixels = secret.getPixels2D();

for (int r = 0; r < secretPixels.length; r++) {
for (int c = 0; c < secretPixels[0].length; c++) {
if (startRow + r < sourcePixels.length &&
startCol + c < sourcePixels[0].length) {
Pixel sourcePixel = sourcePixels[startRow + r][startCol + c];
Pixel secretPixel = secretPixels[r][c];
clearLow(sourcePixel);
setLow(sourcePixel, secretPixel.getColor());
}
}
}
return combined;
}

/**
* Checks if two pictures are identical
*/
public static boolean isSame(Picture p1, Picture p2) {
if (p1.getWidth() != p2.getWidth() || p1.getHeight() != p2.getHeight()) {
return false;
}

Pixel[][] pixels1 = p1.getPixels2D();
Pixel[][] pixels2 = p2.getPixels2D();

for (int r = 0; r < pixels1.length; r++) {
for (int c = 0; c < pixels1[0].length; c++) {
if (!pixels1[r][c].getColor().equals(pixels2[r][c].getColor())) {
return false;
}
}
}
return true;
}

/**
* Finds all different pixels between two pictures
*/
public static ArrayList<Point> findDifferences(Picture p1, Picture p2) {
ArrayList<Point> points = new ArrayList<Point>();
if (p1.getWidth() != p2.getWidth() || p1.getHeight() != p2.getHeight()) {
return points;
}

Pixel[][] pixels1 = p1.getPixels2D();
Pixel[][] pixels2 = p2.getPixels2D();

for (int r = 0; r < pixels1.length; r++) {
for (int c = 0; c < pixels1[0].length; c++) {
if (!pixels1[r][c].getColor().equals(pixels2[r][c].getColor())) {
points.add(new Point(c, r));
}
}
}
return points;
}

/**
* Draws a rectangle around the different area
*/
public static Picture showDifferentArea(Picture p, ArrayList<Point> points) {
Picture copy = new Picture(p);
if (points.size() == 0) return copy;

int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

for (Point point : points) {
int x = (int)point.getX();
int y = (int)point.getY();
if (x < minX) minX = x;
if (x > maxX) maxX = x;
if (y < minY) minY = y;
if (y > maxY) maxY = y;
}

Graphics g = copy.getGraphics();
g.setColor(Color.RED);
g.drawRect(minX, minY, maxX - minX, maxY - minY);

return copy;
}

// Activity 4 Methods

/**
* Encodes a string into numbers
*/
public static ArrayList<Integer> encodeString(String s) {
s = s.toUpperCase();
String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
ArrayList<Integer> result = new ArrayList<Integer>();
for (int i = 0; i < s.length(); i++) {
if (s.substring(i, i+1).equals(" ")) {
result.add(27);
} else {
result.add(alpha.indexOf(s.substring(i, i+1)) + 1);
}
}
result.add(0);
return result;
}

/**
* Decodes numbers into a string
*/
private static String decodeString(ArrayList<Integer> codes) {
String result = "";
String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
for (int i = 0; i < codes.size(); i++) {
if (codes.get(i) == 27) {
result += " ";
} else if (codes.get(i) == 0) {
break;
} else {
result += alpha.substring(codes.get(i)-1, codes.get(i));
}
}
return result;
}

/**
* Splits a number into 3 two-bit pairs
*/
private static int[] getBitPairs(int num) {
int[] bits = new int[3];
int code = num;
for (int i = 0; i < 3; i++) {
bits[i] = code % 4;
code = code / 4;
}
return bits;
}

/**
* Hides text in a picture
*/
public static void hideText(Picture source, String s) {
ArrayList<Integer> codes = encodeString(s);
Pixel[][] pixels = source.getPixels2D();
int codeIndex = 0;
int bitIndex = 0;

for (int r = 0; r < pixels.length && codeIndex < codes.size(); r++) {
for (int c = 0; c < pixels[0].length && codeIndex < codes.size(); c++) {
Pixel p = pixels[r][c];
int[] bits = getBitPairs(codes.get(codeIndex));

clearLow(p);
p.setRed(p.getRed() + bits[0]);
p.setGreen(p.getGreen() + bits[1]);
p.setBlue(p.getBlue() + bits[2]);

codeIndex++;
}
}
}

/**
* Reveals text hidden in a picture
*/
public static String revealText(Picture source) {
Pixel[][] pixels = source.getPixels2D();
ArrayList<Integer> codes = new ArrayList<Integer>();

for (int r = 0; r < pixels.length; r++) {
for (int c = 0; c < pixels[0].length; c++) {
Pixel p = pixels[r][c];
int code = (p.getRed() % 4) +
((p.getGreen() % 4) * 4) +
((p.getBlue() % 4) * 16);

if (code == 0) {
return decodeString(codes);
}
codes.add(code);
}
}
return decodeString(codes);
}

// Activity 5 - Open Ended Example

/**
* Applies a sepia tone filter to a picture
*/
public static Picture applySepia(Picture p) {
Picture newPic = new Picture(p);
Pixel[][] pixels = newPic.getPixels2D();

for (Pixel[] row : pixels) {
for (Pixel pixel : row) {
int r = pixel.getRed();
int g = pixel.getGreen();
int b = pixel.getBlue();

int newRed = (int)(0.393 * r + 0.769 * g + 0.189 * b);
int newGreen = (int)(0.349 * r + 0.686 * g + 0.168 * b);
int newBlue = (int)(0.272 * r + 0.534 * g + 0.131 * b);

pixel.setRed(Math.min(255, newRed));
pixel.setGreen(Math.min(255, newGreen));
pixel.setBlue(Math.min(255, newBlue));
}
}
return newPic;
}

// Main method to test all functionality

public static void main(String[] args) {
// Test all the functionality
Picture beach = new Picture("beach.jpg");

// Test clearLow
Picture cleared = testClearLow(beach);
cleared.explore();

// Test setLow
Picture colored = testSetLow(beach, Color.PINK);
colored.explore();

// Test reveal
Picture revealed = revealPicture(colored);
revealed.explore();

// Test hiding pictures
Picture arch = new Picture("arch.jpg");
Picture robot = new Picture("robot.jpg");
if (canHide(arch, robot)) {
Picture hidden = hidePicture(arch, robot, 65, 208);
hidden.explore();
Picture unhidden = revealPicture(hidden);
unhidden.explore();
}

// Test text hiding
Picture messagePic = new Picture("beach.jpg");
hideText(messagePic, "HELLO WORLD");
messagePic.explore();
String message = revealText(messagePic);
System.out.println("Hidden message: " + message);

// Test open-ended activity
Picture sepia = applySepia(beach);
sepia.explore();

// Test picture comparison
Picture beach2 = new Picture("beach.jpg");
System.out.println("Same picture: " + isSame(beach, beach2));

// Test finding differences
ArrayList<Point> diff = findDifferences(beach, colored);
System.out.println("Number of differences: " + diff.size());
Picture diffPic = showDifferentArea(beach, diff);
diffPic.explore();
}
}
