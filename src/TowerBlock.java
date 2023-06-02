public class TowerBlock {
    private float x;
    private float y;
    private float width;
    private float height;
    private int textureID;

    private float angle;

    public TowerBlock(float x, float y, float width, float height, int textureID) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.textureID = textureID;
        this.angle = 0.0f;
    }


    public static float length(TowerBlock[] towerBlocks) {
        float totalLength = 0.0f;
        for (TowerBlock block : towerBlocks) {
            totalLength += block.getWidth();
        }
        return totalLength;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getTextureID() {
        return textureID;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }
}
