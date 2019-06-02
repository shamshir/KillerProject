package communications;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class GameConfiguration {

    private boolean soundEffects;
    private boolean pacmanActive;
    private boolean soundsMusic;
    private boolean ultraPacman;

    public GameConfiguration() {

    }

    private GameConfiguration(final GameConfiguration.Builder builder) {
        
    }

    public boolean getSoundEffects() {
        return soundEffects;
    }

    public boolean getPacmanActive() {
        return pacmanActive;
    }

    public boolean getSoundsMusic() {
        return soundsMusic;
    }
    
    public boolean getUltraPacman() {
        return ultraPacman;
    }

    public static class Builder {

    private boolean soundEffects;
    private boolean pacmanActive;
    private boolean soundsMusic;
    private boolean ultraPacman;

        public Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }
        
        public Builder soundEffects(final boolean sounds){
            this.soundEffects = sounds;
            return this;
        }
        
        public Builder soundsMusic(final boolean music){
            this.soundsMusic = music;
            return this;
        }
        
        public Builder pacmanActive(final boolean pacmanSounds){
            this.pacmanActive = pacmanSounds;
            return this;
        }
        
        public Builder ultraPacman(final boolean ultraPacman){
            this.ultraPacman = ultraPacman;
            return this;
        }
        
        public GameConfiguration build() {
            return new GameConfiguration(this);
        }
    }
}