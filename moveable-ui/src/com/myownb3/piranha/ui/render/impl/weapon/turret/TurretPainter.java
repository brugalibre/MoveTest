package com.myownb3.piranha.ui.render.impl.weapon.turret;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.myownb3.piranha.core.battle.weapon.turret.cluster.shape.TurretClusterShape;
import com.myownb3.piranha.core.battle.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.ui.render.RenderContext;
import com.myownb3.piranha.ui.render.impl.Drawable;
import com.myownb3.piranha.ui.render.impl.drawmode.ColorSetMode;
import com.myownb3.piranha.ui.render.impl.shape.ShapePainterFactory;

public class TurretPainter extends Drawable<Shape> {

   private List<Drawable<? extends Shape>> shapeGunPainters;
   private List<Drawable<? extends Shape>> shapeGunCarriagePainters;

   public TurretPainter(Shape shape, Color color) {
      super(shape);
      shapeGunPainters = new ArrayList<>();
      shapeGunCarriagePainters = new ArrayList<>();
      if (shape instanceof TurretShape) {
         createAndAddGunAndGunCarriagePainter(color, (TurretShape) shape);
      } else if (shape instanceof TurretClusterShape) {
         TurretClusterShape turretClusterShape = (TurretClusterShape) shape;
         for (Shape turretShape : turretClusterShape.getTurretShapes()) {
            createAndAddGunAndGunCarriagePainter(color, (TurretShape) turretShape);
         }
      }
   }

   private void createAndAddGunAndGunCarriagePainter(Color color, TurretShape turretShape) {
      this.shapeGunPainters.add(ShapePainterFactory.getShapePainter(turretShape.getGunShape(), color, false));
      this.shapeGunCarriagePainters.add(ShapePainterFactory.getShapePainter(turretShape.getGunCarriageShape(), color, false));
   }

   @Override
   public void render(RenderContext graphicsCtx) {
      shapeGunPainters.stream().forEach(renderer -> renderer.render(graphicsCtx));
      shapeGunCarriagePainters.stream().forEach(renderer -> renderer.render(graphicsCtx));
   }

   @Override
   public void setBounds(Rectangle bounds) {
      throw new IllegalStateException("Not allowed here!");
   }

   @Override
   public void setColorSetMode(ColorSetMode drawMode) {
      throw new IllegalStateException("Not allowed here!");
   }

}
