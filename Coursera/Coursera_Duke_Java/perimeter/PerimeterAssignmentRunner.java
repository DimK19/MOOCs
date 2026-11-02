import edu.duke.*;
import java.io.File;

public class PerimeterAssignmentRunner {
    public double getPerimeter (Shape s) {
        // Start with totalPerim = 0
        double totalPerim = 0.0;
        // Start wth prevPt = the last point 
        Point prevPt = s.getLastPoint();
        // For each point currPt in the shape,
        for(Point currPt : s.getPoints()) {
            // Find distance from prevPt point to currPt 
            double currDist = prevPt.distance(currPt);
            // Update totalPerim by currDist
            totalPerim = totalPerim + currDist;
            // Update prevPt to be currPt
            prevPt = currPt;
        }
        // totalPerim is the answer
        return totalPerim;
    }

    // Assignment 1.1a
    public int getNumPoints (Shape s) {
        /* Returns the number of points in Shape s
         * by iterating over them.
        */
        int count = 0;
        for(Point p: s.getPoints()) {
            count++;
        }
        return count;
    }

    // Assignment 1.2a
    public double getAverageLength(Shape s) {
        /* Calculates average length by dividing
         * perimeter by number of points.
        */
        double length = getPerimeter(s);
        int numPoints = getNumPoints(s);
        return length / numPoints;
    }

    // Assignment 2.1a
    public double getLargestSide(Shape s) {
        /* Iterates through all points to find
         * the maximum distance between adjacent points.
        */
        double maxDist = 0.0;
        Point prevPt = s.getLastPoint();
        for(Point currPt: s.getPoints()) {
            double dist = prevPt.distance(currPt);
            if(dist > maxDist) {
                maxDist = dist;
            }
            prevPt = currPt;
        }
        return maxDist;
    }

    // Assignment 2.2a
    public double getLargestX(Shape s) {
        /* Iterates through all points to evaluate
         * and store the highest X coordinate.
         */
        Point lastPt = s.getLastPoint();
        double maxX = lastPt.getX();
        for(Point p : s.getPoints()){
            if(p.getX() > maxX){
                maxX = p.getX();
            }
        }
        return maxX;
    }

    // Assignment 3.1
    public double getLargestPerimeterMultipleFiles() {
        /* Opens multiple files and evaluates which
         * shape yields the highest perimeter.
        */
        DirectoryResource dr = new DirectoryResource();
        double maxPerim = 0.0;
        for(File f: dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            Shape s = new Shape(fr);
            double perim = getPerimeter(s);
            if(perim > maxPerim) {
                maxPerim = perim;
            }
        }
        return maxPerim;
    }

    // Assignment 3.3
    public String getFileWithLargestPerimeter() {
        /* Similar to above, but tracks and returns
         * the name of the file with the largest perimeter.
         */
        DirectoryResource dr = new DirectoryResource();
        File temp = null;    
        double maxPerim = 0.0;
        
        for(File f: dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            Shape s = new Shape(fr);
            double perim = getPerimeter(s);
            if(perim > maxPerim) {
                maxPerim = perim;
                temp = f; // Store the file with the current maximum perimeter
            }
        }
        
        if(temp != null) {
            return temp.getName();
        }
        
        return "";
    }

    // Assignments 1b, 2b (Section 1) and 1b, 2b (Section 2).
    public void testPerimeter () {
        // Updated to include print statements
        FileResource fr = new FileResource();
        Shape s = new Shape(fr);
        double length = getPerimeter(s);
        System.out.println("perimeter = " + length);
        
        System.out.println("number of points = " + getNumPoints(s));
        System.out.println("average length = " + getAverageLength(s));
        System.out.println("largest side = " + getLargestSide(s));
        System.out.println("largest X = " + getLargestX(s));
    }
    
    // Assignment 3.2
    public void testPerimeterMultipleFiles() {
        // Tests the getLargestPerimeterMultipleFiles method.
        double largestPerim = getLargestPerimeterMultipleFiles();
        System.out.println("Largest perimeter across multiple files = " + largestPerim);
    }

    // Assignment 3.4
    public void testFileWithLargestPerimeter() {
        // Tests the getFileWithLargestPerimeter method.
        String fileName = getFileWithLargestPerimeter();
        System.out.println("File with the largest perimeter = " + fileName);
    }

    // This method creates a triangle that you can use to test your other methods
    public void triangle(){
        Shape triangle = new Shape();
        triangle.addPoint(new Point(0,0));
        triangle.addPoint(new Point(6,0));
        triangle.addPoint(new Point(3,6));
        for(Point p: triangle.getPoints()){
            System.out.println(p);
        }
        double peri = getPerimeter(triangle);
        System.out.println("perimeter = " + peri);
    }

    // This method prints names of all files in a chosen folder
    // that you can use to test your other methods
    public void printFileNames() {
        DirectoryResource dr = new DirectoryResource();
        for(File f: dr.selectedFiles()) {
            System.out.println(f);
        }
    }

    public static void main (String[] args) {
        PerimeterAssignmentRunner pr = new PerimeterAssignmentRunner();
        // Uncomment one of the following, depending on each question
        pr.testPerimeter();
        // pr.testPerimeterMultipleFiles();
        // pr.testFileWithLargestPerimeter();
    }
}
