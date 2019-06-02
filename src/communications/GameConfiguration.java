package communications;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class GameConfiguration {

    private boolean sounds;
    private boolean pacmanSounds;
    private boolean music;

    public GameConfiguration() {

    }

    private GameConfiguration(final GameConfiguration.Builder builder) {
        sounds = builder.sounds;
        pacmanSounds = builder.pacmanSounds;
        music = builder.music;        
    }

    public boolean getSounds() {
        return sounds;
    }

    public boolean getPacmanSounds() {
        return pacmanSounds;
    }

    public boolean getMusic() {
        return music;
    }

    public static class Builder {

    private boolean sounds;
    private boolean pacmanSounds;
    private boolean music;

        public Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }
        
        public Builder sounds(final boolean sounds){
            this.sounds = sounds;
            return this;
        }
        
        public Builder music(final boolean music){
            this.music = music;
            return this;
        }
        
        public Builder pacmanSounds(final boolean pacmanSounds){
            this.pacmanSounds = pacmanSounds;
            return this;
        }
        
        public GameConfiguration build() {
            return new GameConfiguration(this);
        }
    }
}