package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.changeTeam(team);
            member.setType(MemberType.ADMIN);
            em.persist(member);

            em.flush();
            em.clear();

            String query = "SELECT " +
                    "CASE " +
                    "WHEN m.age <= 10 THEN '학생요금' " +
                    "WHEN m.age >= 60 THEN '경로요금' " +
                    "ELSE '일반요금' " +
                    "END " +
                    "FROM Member m";
            List<String> result = em.createQuery(query, String.class).getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
