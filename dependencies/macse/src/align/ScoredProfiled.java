package align;

public class ScoredProfiled
{
	BasicProfile profile;
	float SP_AA_score;
	float SP_FS_STOP_score;
	public ScoredProfiled(BasicProfile profile, float SP_AA_score, float SP_FS_STOP_score) {
		this.profile = profile;
		this.SP_AA_score = SP_AA_score;
		this.SP_FS_STOP_score = SP_FS_STOP_score;
	}
	
	public float getTotalSP()
	{
		return this.SP_AA_score + this.SP_FS_STOP_score;
	}
	
	public float getSP_AA_score()
	{
		return this.SP_AA_score ;
	}
	
	public BasicProfile getProfile()
	{
		return this.profile;
	}
}
