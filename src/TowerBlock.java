public class TowerBlock {
        private float x;
        private float y;
        private float width;
        private float height;
        private int textureID;

        public TowerBlock(float x, float y, float width, float height, int textureID) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.textureID = textureID;
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
