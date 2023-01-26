package com.example.dotsandboxes.models.classes;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class TestMain {
    public static void main(String[] args) {
        int n =3, m=4;
        Dot[][] dots = new Dot[n][m];
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++) {
                dots[i][j] = new Dot(i,j);
            }
        }
        LinkedHashSet<Line> lines = new LinkedHashSet<>();
        Line horz = null, vert = null;
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++) {
                if(j < m-1) {
                    horz = new Line(dots[i][j], dots[i][j + 1]);
                }
                if (i<n-1) {
                    vert = new Line(dots[i][j], dots[i + 1][j]);
                }
                if(!lines.contains(horz))
                    lines.add(horz);
                if(!lines.contains(vert))
                    lines.add(vert);
            }
        }
        for (Line line:lines) {
            System.out.println("Line (" + line.getStart().getX() + "," + line.getStart().getY() + "),("+ line.getEnd().getX() + "," + line.getEnd().getY() + ")" );
        }
        System.out.printf("\n\n");
        Object[] lineArray = lines.toArray();
        Line check = (Line) lineArray[11];
        System.out.printf("Checking for Line: (" + check.getStart().getX() + "," + check.getStart().getY() + ") -> (" + check.getEnd().getX() + "," + check.getEnd().getY() +
                ")\n");
        for (Line line:lines) {
            boolean can = check.canFormABox(line);
            System.out.println("Line (" + line.getStart().getX() + "," + line.getStart().getY() + "),("+ line.getEnd().getX() + "," + line.getEnd().getY() + ")" +
                    " can form a box? " + can);
        }
    }
}
