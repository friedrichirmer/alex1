package alex;/* *********************************************************************** *
 * project: simsocsys
 *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2016 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : gregor dot laemmel at gmail dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */


import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;



/**
 */
public class VehicleInfo {

    private final int x;
    private final int y;
    

    private final double phi;


    private final int radius;
	private PVector forceTarget;
	private PVector forceVehicles;
	private PVector forceWalls;
    private double momentSpeed;

    public VehicleInfo(double x, double y, double phi, double radius,
                       PVector forceTarget, PVector forceVehicles, PVector forceWalls, double momentSpeed) {
        this.x = (int) (Vis.scale * x);
        this.y = (int) (Vis.scale * y);
        this.phi = phi;
        this.radius = (int) (Vis.scale * radius);
        this.forceTarget = forceTarget;
        this.forceVehicles = forceVehicles;
        this.forceWalls = forceWalls;
        this.momentSpeed = momentSpeed;
    }
    

    public void draw(PApplet p) {
            p.pushMatrix();
            

            p.translate(x, y);

            p.rotate((float) (phi));

            p.fill(255, 64, 64, 200);

            p.ellipseMode(PConstants.CENTER);
            p.fill(255, 0, 0);
            p.ellipse(radius / 2, -radius / 2, 2, 2);
            p.fill(255, 0, 0);
            p.ellipse(radius / 2, radius / 2, 2, 2);
                if (momentSpeed < 0.3) {
                    p.fill(255, 64, 64, 200);
                } else if (momentSpeed < 0.6) {
                    p.fill(255, 214, 3, 200);
                } else {
                    p.fill(64, 255, 64, 200);
                }
            p.ellipse(radius / 4, 0, radius, radius);
            p.ellipse(radius / 4, 0, 7, 7);

            p.popMatrix();

            /**
             * Pfeile k�nnen die Richtungen und St�rken von Kr�ften anzeigen (bei Bedarf aktivieren)
             *
             * Roter Pfeil zeigt die Kraft in Richtung des aktuellen Ziels an (nur bei Richtungs�nderungen)
             */

            //        p.strokeWeight(2);
            //        p.stroke(255, 0, 0);
            //        p.line((float)x,(float)y ,((float)x + forceTarget.x),(float)y + forceTarget.y);
            //        PVector tarl = forceTarget.get();
            //        PVector tarr = forceTarget.get();
            //        tarl.rotate((float) 2.5);
            //        tarl.setMag((float) 10);
            //        tarr.rotate((float) -2.5);
            //        tarr.setMag((float) 10);
            //        p.line((float)x + forceTarget.x,(float)y + forceTarget.y,(float)x + forceTarget.x + tarr.x,(float)y + forceTarget.y+tarr.y);
            //        p.line((float)x + forceTarget.x,(float)y + forceTarget.y,(float)x + forceTarget.x + tarl.x,(float)y + forceTarget.y+tarl.y);
            //        p.strokeWeight(1);
            //        p.stroke(0, 0, 0);
            
            /**
             * Blauer Pfeil zeigt die abst0�enden Kr�fte von anderen Fahrzeugen
             */

//                    p.strokeWeight(2);
//                    p.stroke(0, 0, 255);
//                    p.line((float)x,(float)y ,((float)x + forceVehicles.x),(float)y + forceVehicles.y);
//                    PVector varl = forceVehicles.get();
//                    PVector varr = forceVehicles.get();
//                    varl.rotate((float) 2.5);
//                    varl.setMag((float) 10);
//                    varr.rotate((float) -2.5);
//                    varr.setMag((float) 10);
//                    p.line((float)x + forceVehicles.x,(float)y + forceVehicles.y,(float)x + forceVehicles.x + varr.x,(float)y + forceVehicles.y + varr.y);
//                    p.line((float)x + forceVehicles.x,(float)y + forceVehicles.y,(float)x + forceVehicles.x + varl.x,(float)y + forceVehicles.y + varl.y);
//                    p.strokeWeight(1);
//                    p.stroke(0, 0, 0);

            /**
             * Gr�ner Pfeil zeigt die Summe der Wandkr�fte auf ein Fahrzeug
             */
          
//            p.strokeWeight(2);
//            p.stroke(0, 255, 0);
//            p.line((float) x, (float) y, ((float) x + forceWalls.x), (float) y + forceWalls.y);
//            PVector warl = forceWalls.get();
//            PVector warr = forceWalls.get();
//            warl.rotate((float) 2.5);
//            warl.setMag((float) 10);
//            warr.rotate((float) -2.5);
//            warr.setMag((float) 10);
//            p.line((float) x + forceWalls.x, (float) y + forceWalls.y, (float) x + forceWalls.x + warr.x, (float) y + forceWalls.y + warr.y);
//            p.line((float) x + forceWalls.x, (float) y + forceWalls.y, (float) x + forceWalls.x + warl.x, (float) y + forceWalls.y + warl.y);
//            p.strokeWeight(1);
//            p.stroke(0, 0, 0);

        }
}
