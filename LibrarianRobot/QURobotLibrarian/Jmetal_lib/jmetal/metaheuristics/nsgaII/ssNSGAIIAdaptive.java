package jmetal.metaheuristics.nsgaII;

import java.util.Comparator;

import core.Algorithm;
import core.Operator;
import core.Problem;
import core.Solution;
import core.SolutionSet;
import qualityIndicator.QualityIndicator;
import util.Distance;
import util.PseudoRandom;
import util.Ranking;
import util.comparators.CrowdingComparator;
import util.comparators.DominanceComparator;
import util.offspring.Offspring;
import util.offspring.PolynomialMutationOffspring;

public class ssNSGAIIAdaptive extends Algorithm {
  public int populationSize_            ;
  public SolutionSet population_        ;
  public SolutionSet offspringPopulation_;
  public SolutionSet union_              ;

  int maxEvaluations_                   ;
  int evaluations_                      ;
  boolean applyMutation_                ; // Polynomial mutation
  double distributionIndexForMutation_  ;
  double distributionIndexForCrossover_ ;
  double crossoverProbability_          ;
  double mutationProbability_           ;
  double CR_ ;
  double F_  ;


  int[] contributionCounter_; // contribution per crossover operator
  double[] contribution_; // contribution per crossover operator
  double total = 0.0;

  int currentCrossover_;
  int[][] contributionArchiveCounter_;
  public double mincontribution = 0.30;

  final boolean TRAZA = false;

  private QualityIndicator indicators_;

  public ssNSGAIIAdaptive(Problem problem) {
    super(problem);
  }

  public SolutionSet execute() throws JMException, ClassNotFoundException {
    double contrDE = 0;
    double contrSBX = 0;
    double contrBLXA = 0;
    double contrPol = 0;
    double contrTotalDE = 0;
    double contrTotalSBX = 0;
    double contrTotalPol = 0;

    double contrReal [] = new double[3] ;
    contrReal[0] = contrReal[1] = contrReal[2] = 0 ;

    Comparator dominance = new DominanceComparator();
    Comparator crowdingComparator = new CrowdingComparator();
    Distance distance = new Distance();

    Operator selectionOperator;

    //Read parameter values
    populationSize_ = ((Integer) getInputParameter("populationSize")).intValue();
    //CR_ = ((Double) getInputParameter("CR")).doubleValue();
    //F_ = ((Double) getInputParameter("F")).doubleValue();
    maxEvaluations_ = ((Integer) getInputParameter("maxEvaluations")).intValue();
    indicators_ = (QualityIndicator) getInputParameter("indicators");

    //Init the variables
    population_ = new SolutionSet(populationSize_);
    evaluations_ = 0;

    selectionOperator = operators_.get("selection");

    Offspring[] getOffspring;
    int N_O; // number of offpring objects

    getOffspring = ((Offspring[]) getInputParameter("offspringsCreators"));
    N_O = getOffspring.length;

    contribution_               = new double[N_O];
    contributionCounter_        = new int[N_O];

    contribution_[0] = populationSize_ / (double) N_O / populationSize_;
    for (int i = 1; i < N_O; i++) {
      contribution_[i] = populationSize_ / (double) N_O / populationSize_ + contribution_[i - 1];
    }

    // Create the initial solutionSet
    Solution newSolution;
    for (int i = 0; i < populationSize_; i++) {
      newSolution = new Solution(problem_);
      problem_.evaluate(newSolution);
      problem_.evaluateConstraints(newSolution);
      evaluations_++;
      newSolution.setLocation(i);
      population_.add(newSolution);
    } //for

    while (evaluations_ < maxEvaluations_) {

      // Create the offSpring solutionSet
      // Create the offSpring solutionSet
      offspringPopulation_ = new SolutionSet(2);
      Solution[] parents = new Solution[2];

      int selectedSolution = PseudoRandom.randInt(0, populationSize_-1) ;
      Solution individual = new Solution(population_.get(selectedSolution));

      int selected = 0;
      boolean found = false ;
      Solution offSpring = null;
      double rnd = PseudoRandom.randDouble();
      for (selected = 0; selected < N_O; selected++) {

        if (!found && (rnd <= contribution_[selected])) {
          if ("DE".equals(getOffspring[selected].id())) {
            offSpring = getOffspring[selected].getOffspring(population_, selectedSolution) ;
            contrDE++;
          } else if ("SBXCrossover".equals(getOffspring[selected].id())) {
            offSpring = getOffspring[selected].getOffspring(population_);
            contrSBX++;
          } else if ("BLXAlphaCrossover".equals(getOffspring[selected].id())) {
            offSpring = getOffspring[selected].getOffspring(population_);
            contrBLXA++;
          } else if ("PolynomialMutation".equals(getOffspring[selected].id())) {
            offSpring = ((PolynomialMutationOffspring)getOffspring[selected]).getOffspring(individual);
            contrPol++;
          } else {
            System.out.println("Error in NSGAIIAdaptive. Operator " + offSpring + " does not exist") ;
          }

          offSpring.setFitness((int) selected);
          currentCrossover_ = selected;
          found = true;
        } // if
      } // for

      problem_.evaluate(offSpring) ;
      offspringPopulation_.add(offSpring);
      evaluations_ +=1 ;

      // Create the solutionSet union of solutionSet and offSpring
      union_ = ((SolutionSet) population_).union(offspringPopulation_);

      // Ranking the union
      Ranking ranking = new Ranking(union_);

      int remain = populationSize_;
      int index = 0;
      SolutionSet front = null;
      population_.clear();

      // Obtain the next front
      front = ranking.getSubfront(index);

      while ((remain > 0) && (remain >= front.size())) {
        //Assign crowding distance to individuals
        distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
        //Add the individuals of this front
        for (int k = 0; k < front.size(); k++) {
          population_.add(front.get(k));
        } // for

        //Decrement remain
        remain = remain - front.size();

        //Obtain the next front
        index++;
        if (remain > 0) {
          front = ranking.getSubfront(index);
        } // if
      } // while

      // Remain is less than front(index).size, insert only the best one
      if (remain > 0) {  // front contains individuals to insert
        distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
        front.sort(new CrowdingComparator());
        for (int k = 0; k < remain; k++) {
          population_.add(front.get(k));
        } // for

        remain = 0;
      } // if

      // CONTRIBUTION CALCULATING PHASE
      if ((evaluations_ % populationSize_) == 0) {
        // First: reset contribution counter
        for (int i = 0; i < N_O; i++) {
          contributionCounter_[i] = 0 ;
        }

        // Second: deteRmine the contribution of each operator
        for (int i = 0; i < population_.size(); i++) {
          if ((int) population_.get(i).getFitness() != -1) {
            contributionCounter_[(int) population_.get(i).getFitness()] += 1;
          }
          population_.get(i).setFitness(-1);
        }

        int minimumContribution = 2;
        int totalContributionCounter = 0;

        for (int i = 0; i < N_O; i++) {
            if (contributionCounter_[i] < minimumContribution) {
                contributionCounter_[i] = minimumContribution;
            }
            totalContributionCounter += contributionCounter_[i];
        }

        // Third: calculating contribution
        contribution_[0] = contributionCounter_[0] * 1.0
                / totalContributionCounter;
        for (int i = 1; i < N_O; i++) {
            contribution_[i] = contribution_[i - 1] + 1.0
         * contributionCounter_[i]
                    / totalContributionCounter;
        }
      }
    } // while


    // Return the first non-dominated front
    Ranking ranking = new Ranking(population_);
    return ranking.getSubfront(0);
  }
}
