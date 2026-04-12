package domein;

import repository.GenericDao;

public class TeamManager {

    private GenericDao<Team> teamRepository;

    public TeamManager(GenericDao<Team> teamRepository) {this.teamRepository = teamRepository;}

    public Team addTeam(Werknemer verantwoordelijke) {
        Team t = new Team(verantwoordelijke);

        teamRepository.startTransaction();
        try {
            teamRepository.insert(t);
            teamRepository.commitTransaction();
        } catch (Exception ex) {
            teamRepository.rollbackTransaction();
            throw ex;
        }
        return t;
    }
}
