package modele;

import modele.Tile.TileManager;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Koopa extends Ennemi {
    // 定义两种状态
    public enum State {
        WALKING,  // 正常行走状态
        SHELL     // 被踩后变为龟壳状态
    }

    private State state;

    public Koopa(int x, int speed, boolean movingRight, TileManager tileManager) {
        // 注意：宽、高参数根据实际情况调整（此处示例为20）
        super(x, 20, 20, speed, movingRight, tileManager, "koopa");
        this.state = State.WALKING;
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
        if (state == State.SHELL) {
            try {
                // 假设龟壳图片路径为"/resources/koopa_sprites/shell.png"
                BufferedImage shellImage = ImageIO.read(getClass().getResourceAsStream("/resources/koopa_sprites/koopa3.png"));
                // 龟壳状态下只用一帧图像
                this.image = new BufferedImage[1];
                this.image[0] = shellImage;
                // 更新碰撞区域（可根据实际尺寸调整）
                this.solidArea.width = shellImage.getWidth();
                this.solidArea.height = shellImage.getHeight();
                // 龟壳状态下让其默认向右移动（若需要，可在此设定固定方向）
                this.movingRight = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void moveEnnemi() {
        // 无论处于哪种状态，都采用父类的移动逻辑
        // 父类的moveEnnemi()在碰到障碍物或坑时会自动调头
        super.moveEnnemi();
    }
}
