package dao;

import entity.Product;
import utility.DBUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    @Override
    public void insert(Product p) {
        try (Connection conn = DBUtility.openConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO product(id, name, status, price, description, expiration, category_id) VALUES (?,?,?,?,?,?,?)");

            ps.setString(1, p.getId());
            ps.setString(2, p.getName());
            ps.setBoolean(3, p.isStatus());
            ps.setFloat(4, p.getPrice());
            ps.setString(5, p.getDescription());
            ps.setDate(6, p.getExpiration());
            ps.setString(7, p.getCategoryId());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBUtility.openConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM product");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getString("id"));
                p.setName(rs.getString("name"));
                p.setStatus(rs.getBoolean("status"));
                p.setPrice(rs.getFloat("price"));
                p.setDescription(rs.getString("description"));
                p.setExpiration(readExpiration(rs));
                p.setCategoryId(rs.getString("category_id"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Product findById(String id) {
        Product p = null;
        try (Connection conn = DBUtility.openConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM product WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p = new Product();
                p.setId(rs.getString("id"));
                p.setName(rs.getString("name"));
                p.setStatus(rs.getBoolean("status"));
                p.setPrice(rs.getFloat("price"));
                p.setDescription(rs.getString("description"));
                p.setExpiration(readExpiration(rs));
                p.setCategoryId(rs.getString("category_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    @Override
    public List<Product> findByCategory(String categoryId) {
        List<Product> list = new ArrayList<>();

        try (Connection conn = DBUtility.openConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM product WHERE category_id = ?");

            ps.setString(1, categoryId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getString("id"));
                p.setName(rs.getString("name"));
                p.setStatus(rs.getBoolean("status"));
                p.setPrice(rs.getFloat("price"));
                p.setDescription(rs.getString("description"));
                p.setExpiration(readExpiration(rs));
                p.setCategoryId(rs.getString("category_id"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void update(Product p) {
        try (Connection conn = DBUtility.openConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE product SET name=?, status=?, price=?, description=?, expiration=?, category_id=? WHERE id=?");

            ps.setString(1, p.getName());
            ps.setBoolean(2, p.isStatus());
            ps.setFloat(3, p.getPrice());
            ps.setString(4, p.getDescription());
            ps.setDate(5, p.getExpiration());
            ps.setString(6, p.getCategoryId());
            ps.setString(7, p.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        try (Connection conn = DBUtility.openConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM product WHERE id = ?");

            ps.setString(1, id);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> sortByPrice() {
        List<Product> list = new ArrayList<>();

        try (Connection conn = DBUtility.openConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM product ORDER BY price DESC");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getString("id"));
                p.setName(rs.getString("name"));
                p.setStatus(rs.getBoolean("status"));
                p.setPrice(rs.getFloat("price"));
                p.setDescription(rs.getString("description"));
                p.setExpiration(readExpiration(rs));
                p.setCategoryId(rs.getString("category_id"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Product> sortByName() {
        List<Product> list = new ArrayList<>();

        try (Connection conn = DBUtility.openConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM product ORDER BY name ASC");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getString("id"));
                p.setName(rs.getString("name"));
                p.setStatus(rs.getBoolean("status"));
                p.setPrice(rs.getFloat("price"));
                p.setDescription(rs.getString("description"));
                p.setExpiration(readExpiration(rs));
                p.setCategoryId(rs.getString("category_id"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Product> expiringSoon() {
        List<Product> list = new ArrayList<>();

        try (Connection conn = DBUtility.openConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM product " +
                    "WHERE expiration BETWEEN GETDATE() " +
                    "AND DATEADD(DAY, 3, GETDATE())");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getString("id"));
                p.setName(rs.getString("name"));
                p.setStatus(rs.getBoolean("status"));
                p.setPrice(rs.getFloat("price"));
                p.setDescription(rs.getString("description"));
                p.setExpiration(readExpiration(rs));
                p.setCategoryId(rs.getString("category_id"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean existsByName(String name) {
        try (Connection conn = DBUtility.openConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(1) FROM product WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private java.sql.Date readExpiration(ResultSet rs) throws SQLException {
        try {
            return rs.getDate("expiration");
        } catch (SQLException ex) {
            return rs.getDate("date");
        }
    }
}
