var express = require('express');
var router = express.Router();
const glpk = require('../glpk.js');

/* GET users listing. */
router.get('/', function(req, res, next) {
  let lp = {
    name: 'LP',
    objective: {
      direction: glpk.GLP_MAX,
      name: 'obj',
      vars: [
        { name: 'x1', coef: 0.6 },
        { name: 'x2', coef: 0.5 }
      ]
    },
    subjectTo: [
      {
        name: 'cons1',
        vars: [
          { name: 'x1', coef: 1.0 },
          { name: 'x2', coef: 2.0 }
        ],
        bnds: { type: glpk.GLP_UP, ub: 1.0, lb: 0.0 }
      },
      {
        name: 'cons2',
        vars: [
          { name: 'x1', coef: 3.0 },
          { name: 'x2', coef: 1.0 }
        ],
        bnds: { type: glpk.GLP_UP, ub: 2.0, lb: 0.0 }
      }
    ]
  };
  
  const response = glpk.solve(lp, glpk.GLP_MSG_ALL)
  
  res.send(response);
});

module.exports = router;
