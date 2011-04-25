package org.sanyanse.common;


import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GraphDecomposition
{
  private Matrix _adjacency;
  private Matrix _degree;
  private volatile Matrix _laplacian = null;
  private volatile EigenvalueDecomposition _adjSpectrum = null;
  private volatile EigenvalueDecomposition _lapSpectrum = null;

  private ReentrantReadWriteLock _rwlLaplacian = new ReentrantReadWriteLock();
  private ReentrantReadWriteLock _rwlAdjSpectrum = new ReentrantReadWriteLock();
  private ReentrantReadWriteLock _rwlLapSpectrum = new ReentrantReadWriteLock();

  private int _length;

  public GraphDecomposition(Matrix adj, Matrix deg)
  {
    _adjacency = adj;
    _degree = deg;
    _length = _adjacency.getColumnDimension();
  }

  public int getLength()
  {
    return _length;
  }

  public int getHeight()
  {
    return _length;
  }

  public Matrix getAdjacency()
  {
    return _adjacency;
  }

  public Matrix getDegree()
  {
    return _degree;
  }

  public Map<String, Double> getCentrality(Graph graph)
  {
    Map<String, Double> metric = new HashMap<String, Double>(graph.NodeCount);
    EigenvalueDecomposition e = getAdjacencySpectrum();
    Matrix V = e.getV();

    for (int i = 0; i < _length; i++)
    {
      metric.put(graph.Nodes[i].Id, V.get(i, _length - 1));
    }

    return metric;
  }

  public Matrix getLaplacian()
  {
    Matrix result;

    _rwlLaplacian.readLock().lock();

    if (_laplacian == null)
    {
      _rwlLaplacian.readLock().unlock();
      _rwlLaplacian.writeLock().lock();

      if (_laplacian == null)
      {
        _laplacian = _degree.minus(_adjacency);
      }

      _rwlLaplacian.readLock().lock();
      _rwlLaplacian.writeLock().unlock();
    }

    result = _laplacian;

    _rwlLaplacian.readLock().unlock();

    return result;
  }

  public EigenvalueDecomposition getAdjacencySpectrum()
  {
    EigenvalueDecomposition result;

    _rwlAdjSpectrum.readLock().lock();

    if (_adjSpectrum == null)
    {
      _rwlAdjSpectrum.readLock().unlock();
      _rwlAdjSpectrum.writeLock().lock();

      if (_adjSpectrum == null)
      {
        _adjSpectrum = _adjacency.eig();
      }

      _rwlAdjSpectrum.readLock().lock();
      _rwlAdjSpectrum.writeLock().unlock();
    }

    result = _adjSpectrum;

    _rwlAdjSpectrum.readLock().unlock();

    return result;
  }

  public EigenvalueDecomposition getLaplacianSpectrum()
  {
    EigenvalueDecomposition result;

    _rwlLapSpectrum.readLock().lock();

    if (_lapSpectrum == null)
    {
      _rwlLapSpectrum.readLock().unlock();
      _rwlLapSpectrum.writeLock().lock();

      if (_lapSpectrum == null)
      {
        _lapSpectrum = getLaplacian().eig();
      }

      _rwlLapSpectrum.readLock().lock();
      _rwlLapSpectrum.writeLock().unlock();
    }

    result = _lapSpectrum;

    _rwlLapSpectrum.readLock().unlock();

    return result;
  }

  private static GraphDecomposition computeGraphDecomposition(Graph graph)
  {
    Matrix adj = new Matrix(graph.NodeCount, graph.NodeCount);
    Matrix deg = new Matrix(graph.NodeCount, graph.NodeCount);

    ColorableNode[] nodes = graph.Nodes;
    Map<String, Set<ColorableNode>> edgeMap = graph.EdgeMap;

    int nodeCnt = graph.NodeCount;

    for (int i = 0; i < nodeCnt; i++)
    {
      String nodeId = nodes[i].Id;

      ColorableNode[] xEdges = nodes[i].Edges;

      deg.set(i, i, xEdges.length);

      for (int j = 0; j < nodeCnt; j++)
      {
        adj.set(i, j, edgeMap.get(nodeId).contains(nodes[j].Id) ? 1.0 : 0);
      }
    }

    GraphDecomposition comp = new GraphDecomposition(adj, deg);

    return comp;
  }
}
