package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.changeTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.changeTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.changeTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            //엔티티 직접 사용 - 기본 키를 사용함
            String query1 = "SELECT m FROM Member m WHERE m = :member";
            Member findMember1 = em.createQuery(query1, Member.class)
                    .setParameter("member", member1)
                    .getSingleResult();
            System.out.println("findMember1 = " + findMember1);

            //기본 키 사용
            String query2 = "SELECT m FROM Member m WHERE m.id = :memberId";
            Member findMember2 = em.createQuery(query2, Member.class)
                    .setParameter("memberId", member2.getId())
                    .getSingleResult();
            System.out.println("findMember2 = " + findMember2);

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
