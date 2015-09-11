----------Pixel3D Readme-------------
Created by ClearWave Interactive, LLC
-------------------------------------

-------------------------------------
NOTICE & SUGGESTED USE:
-------------------------------------

This package was intended to be used for LOW-RESOLUTION, animated, pixel art sprites that 
have been animated frame by frame.  Please DO NOT ATTEMPT TO RENDER HIGH-RESOLUTION IMAGES.  
Rendering images larger than 128x128 is currently unsupported, as it will have a significant
negative performance impact.  However, we would like to Increase this size in the future.

For example, an image of 64x64 will result in nearly 49,152 triangles being drawn to the screen!  
An image of 512x512 will result in over 3,145,728 triangles!  (that's THREE MILLION)

-------------------------------------
Sprite Import Instructions:
-------------------------------------

Sprites must be imported with the following settings in order to work properly with Pixel3D.
After importing & setting up your sprites (or sprite sheets), go to the import settings:

1) Change the Texture Type to Advanced
2) Make sure Non Power of 2 is set to "None"
3) Enable "Read/Write enabled"
4) Enable "Alpha is Transparency"
5) Sprite Mode (your choice, either "Single" or "Multiple")
6) Set "Mesh Type" to "Full Rect" (Important to make sure animations appear appropriately)
7) Set Generate Mip Maps to FALSE

-------------------------------------
Usage Instructions:
-------------------------------------

1. Create a Unity 2D sprite 
2. (Optional) Set up any animations & animator
3. Add the Sprite3D component
4. PRESS PLAY (Sprite3D is not currently visible from the editor).
5. Adjust the size, thickness, and layer offset as desired
6. BEFORE EXITING PLAY MODE Right click the component, and copy
7. After exiting play mode, right click the component, and "Paste Component as Values"

Now you're ready to play!