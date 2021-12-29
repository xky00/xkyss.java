package xkyss.extensions.java.lang.Character;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import java.lang.Character;

@Extension
public class CharacterExt {

  public static int intValue(@This Character self) {
    return self.hashCode();
  }

  public static int intValue(@This Character self, Boolean b) {
    return self.intValue();
//    return self.intValue() + b.intValue();
  }
}