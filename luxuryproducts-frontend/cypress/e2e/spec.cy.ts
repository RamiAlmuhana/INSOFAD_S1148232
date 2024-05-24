describe('Promo Code Test in Cart Page with Mock Data', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [
        {
          "id": 1,
          "name": "Tom Clancy's Rainbow Six Siege",
          "description": "Tom Clancy's Rainbow Six® Siege is an elite, tactical team-based shooter where superior planning and execution triumph.",
          "price": 15.99,
          "imgURL": "https://store.ubisoft.com/on/demandware.static/-/Sites-masterCatalog/default/dw63e24d90/images/large/56c494ad88a7e300458b4d5a.jpg",
          "specifications": "OS *: Originally released for Windows 7, the game can be played on Windows 10 and Windows 11 OSProcessor: Intel Core i5-2500K @ 3.3 GHz or better or AMD FX-8120 @ 3.1 Ghz or better\nMemory: 8 GB RAM\nGraphics: NVIDIA GeForce GTX 670 or AMD Radeon HD 7970",
          "publisher": "Ubisoft",
          "releaseDate": "1-Dec-2015",
          "categoryId": 1,
          "categoryName": "FPS"
        }
      ]
    }).as('getProducts');

    cy.intercept('GET', '/api/promocodes/validate?code=SUMMER2024', {
      statusCode: 200,
      body: {
        discount: 15,
        type: 'PERCENTAGE',
        minSpendAmount: 0,
        startDate: '2024-01-01T00:00:00Z',
        expiryDate: '2024-12-31T23:59:59Z'
      }
    }).as('validatePromoCode');


    cy.intercept('POST', '/auth/login', {
      statusCode: 200,
      body: {
        email: 'test@mail.com',
        token: 'fake-jwt-token'
      }
    }).as('login');
  });

  it('should login, add a product to the cart and apply a promo code', () => {

    cy.visit('http://localhost:4200/auth/login');


    cy.contains('label', 'Email').siblings('input').type('test@mail.com');
    cy.contains('label', 'Password').siblings('input').type('Test123!');
    cy.get('button').contains('Login').click();


    cy.wait('@login').its('response.statusCode').should('eq', 200);


    cy.url().should('include', '/products');


    cy.wait('@getProducts').its('response.statusCode').should('eq', 200);


    cy.contains('button', 'Buy').click();


    cy.visit('http://localhost:4200/cart');


    cy.get('input[name="promoCode"]').type('SUMMER2024');
    cy.get('button').contains('Apply').click();


    cy.wait('@validatePromoCode').its('response.statusCode').should('eq', 200);


    cy.get('.lead').contains('Discounted Price:').should('exist');
  });
});
